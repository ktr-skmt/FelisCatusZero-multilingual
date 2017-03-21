package uima.cc.qalab

import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files

import org.apache.uima.cas.FSIterator
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import us.feliscat.text.StringOption
import us.feliscat.types._

import scala.collection.mutable.ListBuffer
import scala.xml.{Elem, PrettyPrinter, XML}

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQALabExtractionSubtaskDocumentCasConsumer
  extends MultiLingualQALabExtractionSubtask {
  override protected val mSubtaskOpt = StringOption("Extraction Subtask")

  override protected def runId: String = {
    mRunIdOpt match {
      case Some(runId) =>
        f"${runId + 1}%02d"
      case None =>
        "02"
    }
  }

  override protected def process(aJCas: JCas): Unit = {
    println("Document")
    @SuppressWarnings(Array[String]("rawtypes"))
    val itExam: FSIterator[Nothing] = aJCas.getAnnotationIndex(Exam.`type`).iterator(true)
    while (itExam.hasNext) {
      val exam: Exam = itExam.next
      val questionSet: FSArray = exam.getQuestionSet
      if (0 < questionSet.size) {
        val builder = new StringBuilder()
        val printWriter = new PrintWriter(
          Files.newBufferedWriter(
            getOutputFilePath(exam.getLabel), StandardCharsets.UTF_8))
        builder.append("<TOPIC_SET>")
        for (i <- 0 until questionSet.size) {
          val question: Question = questionSet.get(i).asInstanceOf[Question]
          builder.append(s"""<TOPIC ID="${question.getDocument.getId}"><PASSAGE_SET>""")
          val querySet: FSArray = question.getQuerySet
          val elemBuffer = ListBuffer.empty[Elem]

          val documentBuffer = ListBuffer.empty[(Document, Double, Boolean)]

          for (j <- 0 until querySet.size) {
            querySet.get(j) match {
              case query: KeywordQuery =>
                val documentSet: FSArray = query.getKeyword.getDocumentSet
                for (k <- 0 until documentSet.size) {
                  val document: Document = documentSet.get(k).asInstanceOf[Document]
                  val score: Double = scoreForText(question.getDocument, document)
                  documentBuffer += ((document, score, true))
                }
              case query: BoWQuery =>
                val documentSet: FSArray = query.getIndriQuery.getDocumentSet
                for (k <- 0 until documentSet.size) {
                  val document: Document = documentSet.get(k).asInstanceOf[Document]
                  val score: Double = document.getScoreList(0).getScore
                  documentBuffer += ((document, score, false))
                }
              case _ =>
              // Do nothing
            }
          }
          val documentArray: Array[(Document, Double, Boolean)] = documentBuffer.result.
            sortWith((a, b) => a._2 > b._2).toArray
          for (j <- documentArray.indices) {
            val document: Document = documentArray(j)._1
            val score: Double = documentArray(j)._2
            val normalizedScore: Double = {
              if (documentArray(j)._3) {
                score
              } else {
                normalizeScoreWithBoW(score)
              }
            }
            elemBuffer += {
              <PASSAGE
              RANK={(j + 1).toString}
              SOURCE_ID={document.getDocno}
              SOURCE_ID_TYPE={sourceIdType(document.getDocno)}
              SCORE={score.toString}
              NORMALIZED_SCORE={normalizedScore.toString}>
                {reviseText(document.getDocno, document.getTitle, document.getText)}
              </PASSAGE>
            }
          }

          elemBuffer.result foreach {
            elem: Elem =>
              builder.append(elem.toString)
          }
          builder.append("</PASSAGE_SET></TOPIC>")
        }
        builder.append("</TOPIC_SET>")
        val printer = new PrettyPrinter(10000, 2)
        val xml: Elem = XML.loadString(builder.result)
        printWriter.println("""<?xml version="1.0" encoding="UTF-8"?>""")
        printWriter.print(printer.format(xml))
        printWriter.close()
      }
    }
  }
}
