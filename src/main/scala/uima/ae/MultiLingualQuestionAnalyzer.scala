package uima.ae

import java.util.Locale

import jeqa.types._
import org.apache.uima.cas.FSIterator
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import text.StringOption
import time.TimeTmp
import util.uima.FSListUtils._
import util.uima.{FeatureStructure, JCasUtils}
import util.uima.SeqStringUtils._
import util.uima.SeqUtils._

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQuestionAnalyzer extends MultiLingualDocumentAnnotator {
  protected def extractTime(sentenceList: Seq[Sentence]): TimeTmp
  protected def extractGeography(sentenceList: Seq[Sentence]): (Seq[String], Seq[String])
  protected def analyzeQuestionFocus(sentenceSet: Seq[Sentence]): Seq[String]
  protected def generateQuery(aJCas: JCas, question: Question): Seq[Query]

  def process(aJCas: JCas): Unit = {
    println(s">> ${new Locale(localeId).getDisplayLanguage} Question Analyzer Processing")
    val aView: JCas = aJCas.getView(localeId)
    JCasUtils.setAJCasOpt(Option(aView))

    @SuppressWarnings(Array[String]("rawtypes"))
    val itExam: FSIterator[Nothing] = aView.getAnnotationIndex(Exam.`type`).iterator(true)
    while (itExam.hasNext) {
      val exam: Exam = itExam.next
      println("Dataset:")
      print("* ")
      println(exam.getLabel)
      println("Question:")
      val questionSet: FSArray = exam.getQuestionSet
      for (i <- 0 until questionSet.size) {
        val question: Question = questionSet.get(i).asInstanceOf[Question]
        print("- ")
        println(question.getLabel)
        val document: Document = question.getDocument
        if (StringOption(document.getText).nonEmpty) {

          annotate(aView, document, Nil)
          document.setTitle(question.getLabel)

          question.setDocument(document)

          val sentenceList: Seq[Sentence] = document.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]

          //文脈的な時間解析
          val sentenceArray: Array[Sentence] = sentenceList.toArray
          for (j <- 1 until sentenceArray.length) {
            val sentence: Sentence = sentenceArray(j)
            if (Option(sentence.getBeginTime).isEmpty && Option(sentence.getEndTime).isEmpty) {
              val previousSentence: Sentence = sentenceArray(j - 1)
              sentence.setBeginTime(previousSentence.getBeginTime)
              sentence.setEndTime(previousSentence.getEndTime)
            }
          }

          //begin {time limit analysis}
          val timeLimit: TimeTmp = extractTime(sentenceList)
          timeLimit.beginTime match {
            case Some(beginTime) =>
              val beginTimeLimit = FeatureStructure.create[Time]
              beginTimeLimit.setYear(beginTime)
              beginTimeLimit.setTextList(timeLimit.beginTimeTextList.toStringList)
              question.setBeginTimeLimit(beginTimeLimit)
            case None =>
            // Do nothing
          }

          timeLimit.endTime match {
            case Some(endTime) =>
              val endTimeLimit = FeatureStructure.create[Time]
              endTimeLimit.setYear(endTime)
              endTimeLimit.setTextList(timeLimit.endTimeTextList.toStringList)
              question.setEndTimeLimit(endTimeLimit)
            case None =>
            // Do nothing
          }
          //end {time limit analysis}

          //begin {geography limit}
          val (areaList, termList): (Seq[String], Seq[String]) = extractGeography(sentenceList)
          val geographyLimit = FeatureStructure.create[Geography]
          geographyLimit.setTermList(termList.toStringList)
          geographyLimit.setArea(areaList.toStringList)
          question.setGeographyLimit(geographyLimit)
          //end {geography limit}

          //begin {question focus}
          question.setQuestionFocusSet(analyzeQuestionFocus(sentenceList).toStringList)
          //end {question focus}

          //begin {question format type}
          question.setQuestionFormatType(
            if (question.getKeywordSet.toSeq.nonEmpty) {
              "essayWithKeywords"
            } else {
              "essayWithoutKeyword"
            }
          )
          //end {question format type}

          //begin {answer format type}
          question.setAnswerFormatType("essay")
          //end {answer format type}

          //begin {lexical answer type}
          //question.setLexicalAnswerTypeSet(document.getContentWordList)
          //end {lexical answer type}

          //begin {semantic answer type}
          //question.setSemanticAnswerTypeSet(document.getContentWordList)
          //end {semantic answer type}

          //begin {query}
          question.setQuerySet(generateQuery(aView, question).toFSArray)
          //end {query}
        }
      }
    }
  }
}
