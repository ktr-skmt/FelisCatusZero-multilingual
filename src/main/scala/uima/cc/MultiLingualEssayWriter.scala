package uima.cc

import java.util.Locale

import jeqa.types.{Answer, Document, Exam, Question}
import m17n.MultiLingual
import org.apache.uima.cas.{CAS, FSIterator}
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import util.StringUtils._
import util.uima.FSListUtils._
import util.uima.JCasUtils

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.xml.{Elem, XML}

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualEssayWriter extends MultiLingual {
  def process(aCAS: CAS): mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Elem]]]] = {
    println(s">> ${new Locale(localeId).getDisplayLanguage} Essay Writer Processing")
    val aJCas: JCas = aCAS.getView(localeId).getJCas
    JCasUtils.setAJCasOpt(Option(aJCas))
    process(aJCas)
  }

  //exam -> writer -> question -> locale
  private def process(aJCas: JCas): mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Elem]]]] = {
    //exam -> writer -> question -> locale
    val map = mutable.LinkedHashMap.empty[String, mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Elem]]]]
    val itExam: FSIterator[Nothing] = aJCas.getAnnotationIndex(Exam.`type`).iterator(true)
    while (itExam.hasNext) {
      val exam: Exam = itExam.next
      println("Dataset:")
      print("* ")
      println(exam.getLabel)
      map(exam.getLabel) = process(exam)
    }
    map
  }

  private def process(exam: Exam): mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Elem]]] = {
    val questionSet: FSArray = exam.getQuestionSet
    //writer -> question -> locale
    val writerQuestionAnswer = mutable.LinkedHashMap.empty[String, mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Elem]]]
    println("Question:")
    for (i <- 0 until questionSet.size) {
      val question: Question = questionSet.get(i).asInstanceOf[Question]
      print("- ")
      println(question.getLabel)
      val answerSet: Seq[Answer] = question.getAnswerSet.toSeq.asInstanceOf[Seq[Answer]]
      val writerAnswerMap = mutable.LinkedHashMap.empty[String, String]
      val writerGoldStandardListBuffer = ListBuffer.empty[(String, String)]

      answerSet foreach {
        answer: Answer =>
          if (answer.getIsGoldStandard) {
            val document: Document = answer.getDocument
            writerGoldStandardListBuffer += ((answer.getWriter, document.getText))
//            println("Gold Standard")
//            println(answer.getWriter)
//            println(document.getText)
          } else {
            val document: Document = answer.getDocument
            writerAnswerMap(answer.getWriter) = document.getText
//            println("System Answer")
//            println(answer.getWriter)
//            println(document.getText)
          }
      }
      val writerGoldStandardList: Seq[(String, String, Boolean)] =
        writerGoldStandardListBuffer.result map (element => (element._1, element._2, true))
      val xml: Elem = XML.loadString(question.getXml)

      writerAnswerMap foreach {
        case (writer, text) =>
          if (!(writerQuestionAnswer contains writer)) {
            writerQuestionAnswer(writer) = mutable.LinkedHashMap.empty[String, mutable.LinkedHashMap[String, Elem]]
          }
          if (!(writerQuestionAnswer(writer) contains question.getLabel)) {
            writerQuestionAnswer(writer)(question.getLabel) = mutable.LinkedHashMap.empty[String, Elem]
          }
          val localeElem: Elem = getAnswers(
            writerGoldStandardList :+ (writer, text, false),
            xml)
          writerQuestionAnswer(writer)(question.getLabel)(localeId) = localeElem
          //println(localeElem)
        case _ =>
        // Do nothing
      }
    }
    writerQuestionAnswer
  }

  protected def getAnswers(answers: Seq[(String, String, Boolean)], localeElem: Elem): Elem = {
    XML.loadString(localeElem.toString.replaceAllLiteratim("\n", "").
      replaceAll("<expression[ >][^<]*</expression>", "").
      replaceAll("<expression[^/>]*/>", "").
      replaceFirst(
        """<expression_set>[^<]*</expression_set>""",
        s"""<expression_set>${getAnswers(answers)}</expression_set>"""
      )
    )
  }

  protected def getAnswers(answers: Seq[(String, String, Boolean)]): String = {
    val builder = new StringBuilder()
    answers foreach {
      case (writer, answer, isGoldStandard) =>
        builder.append(getExpression(writer, answer, isGoldStandard))
      case _ =>
      // Do nothing
    }
    builder.result
  }

  protected def getExpression(writer: String, answer: String, isGoldStandard: Boolean): String = {
    s"""<expression is_gold_standard="$isGoldStandard" writer="$writer">$answer</expression>"""
  }
}
