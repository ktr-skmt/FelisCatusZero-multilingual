package uima.cc.qalab

import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files

import org.apache.uima.cas.FSIterator
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import us.feliscat.text.StringOption
import us.feliscat.types._
import us.feliscat.util.uima.JCasID
import us.feliscat.util.uima.fsList.FSListUtils

import scala.collection.mutable.ListBuffer
import scala.xml.{Elem, PrettyPrinter, XML}

/**
  * <pre>
  * Created on 2017/02/18.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQALabExtractionSubtaskSentenceCasConsumer
  extends MultiLingualQALabExtractionSubtask {
  override protected val mSubtaskOpt = StringOption("Extraction Subtask")

  override protected def process(aJCas: JCas)(implicit id: JCasID): Unit = {
    println("Sentence")
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

          val sentenceBuffer = ListBuffer.empty[(Sentence, Double, Boolean)]

          for (j <- 0 until querySet.size) {
            querySet.get(j) match {
              case query: KeywordQuery =>
                val documentSet: FSArray = query.getKeyword.getDocumentSet
                for (k <- 0 until documentSet.size) {
                  val document: Document = documentSet.get(k).asInstanceOf[Document]
                  val sentenceList: Seq[Sentence] = document.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]
                  sentenceList foreach {
                    sentence: Sentence =>
                      if (need(question, Option(sentence.getBeginTime), Option(sentence.getEndTime))) {
                        val score: Double = scoreForText(question.getDocument, sentence)
                        sentence.setDocno(document.getDocno)
                        sentence.setTitle(document.getTitle)
                        sentenceBuffer += ((sentence, score, true))
                      }
                  }
                }
              case query: BoWQuery =>
                val documentSet: FSArray = query.getIndriQuery.getDocumentSet
                for (k <- 0 until documentSet.size) {
                  val document: Document = documentSet.get(k).asInstanceOf[Document]
                  val sentenceList: Seq[Sentence] = document.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]
                  sentenceList foreach {
                    sentence: Sentence =>
                      if (need(question, Option(sentence.getBeginTime), Option(sentence.getEndTime))) {
                        val score: Double = document.getScoreList(0).getScore *
                          scoreForText(question.getDocument, sentence)
                        sentence.setDocno(document.getDocno)
                        sentence.setTitle(document.getTitle)
                        sentenceBuffer += ((sentence, score, false))
                      }
                  }
                }
              case _ =>
              // Do nothing
            }
          }
          val sentenceArray: Array[(Sentence, Double, Boolean)] = sentenceBuffer.result.
            sortWith((a, b) => a._2 > b._2).toArray
          for (j <- sentenceArray.indices) {
            val sentence: Sentence = sentenceArray(j)._1
            val score: Double = sentenceArray(j)._2
            val normalizedScore: Double = {
              if (sentenceArray(j)._3) {
                score
              } else {
                normalizeScoreWithBoW(score)
              }
            }
            elemBuffer += {
              <PASSAGE
              RANK={(j + 1).toString}
              SOURCE_ID={sentence.getDocno}
              SOURCE_ID_TYPE={sourceIdType(sentence.getDocno)}
              SCORE={score.toString}
              NORMALIZED_SCORE={normalizedScore.toString}>
                {reviseText(sentence.getDocno, sentence.getTitle, sentence.getText)}
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
