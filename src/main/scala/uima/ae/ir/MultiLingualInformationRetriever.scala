package uima.ae.ir

import java.util.Locale

import org.apache.uima.cas.FSIterator
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import uima.modules.common.MultiLingualDocumentAnnotator
import us.feliscat.types._
import us.feliscat.util.uima.JCasUtils
import us.feliscat.util.uima.time._

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualInformationRetriever extends MultiLingualDocumentAnnotator {
  protected val mIndriScoreIndex: Int = 0
  protected var mDocumentId: Long = 0L

  protected def retrieveByKeyword(aJCas: JCas,
                                  query: KeywordQuery,
                                  keywordCorrectionMap: mutable.Map[String, Seq[String]]): Option[Long]

  protected def retrieveByBoW(aJCas: JCas,
                              query: BoWQuery): Option[Long]

  protected def correctDocByKeyword(aJCas: JCas,
                                    query: KeywordQuery,
                                    keywordCorrectionMap: Map[String, Seq[String]],
                                    beginTimeLimit: Option[Int],
                                    endTimeLimit: Option[Int],
                                    geographyLimit: Option[Geography]): Unit

  protected def correctDocByBoW(aJCas: JCas,
                                query: BoWQuery,
                                beginTimeLimit: Option[Int],
                                endTimeLimit: Option[Int],
                                geographyLimit: Option[Geography]): Unit

  def processQuestion(aView: JCas, question: Question): Unit = {
    print("- ")
    println(question.getLabel)
    val beginTimeLimit: Option[Int] = question.getBeginTimeLimit.toYearOpt
    val endTimeLimit:   Option[Int] = question.getEndTimeLimit.toYearOpt
    val geographyLimit = Option.empty[Geography]
    val keywordCorrectionMap = mutable.Map.empty[String, Seq[String]]

    // Retrieve relevant documents
    val queryArray: FSArray = question.getQuerySet
    for (j <- 0 until queryArray.size) {
      queryArray.get(j) match {
        case query: KeywordQuery if !query.getAlreadyFinishedRetrieving =>
          retrieveByKeyword(
            aView,
            query,
            keywordCorrectionMap
          ) match {
            case Some(docId) =>
              mDocumentId = docId
            case None =>
            // Do nothing
          }
        case query: BoWQuery if !query.getAlreadyFinishedRetrieving =>
          retrieveByBoW(
            aView,
            query
          ) match {
            case Some(docId) =>
              mDocumentId = docId
            case None =>
            // Do nothing
          }
        case _ =>
        // Do nothing
      }
    }

    // Re-correct document, passage and us.feliscat.sentence in other keywords
    for (j <- 0 until queryArray.size) {
      queryArray.get(j) match {
        case query: KeywordQuery if !query.getAlreadyFinishedCorrecting =>
          correctDocByKeyword(
            aView,
            query,
            keywordCorrectionMap.toMap,
            beginTimeLimit,
            endTimeLimit,
            geographyLimit)
        case query: BoWQuery if !query.getAlreadyFinishedCorrecting =>
          correctDocByBoW(
            aView,
            query,
            beginTimeLimit,
            endTimeLimit,
            geographyLimit)
        case _ =>
        // Do nothing
      }
    }
  }

  def process(aJCas: JCas): Unit = {
    println(s">> ${new Locale(localeId).getDisplayLanguage} Information Retriever Processing")
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
        processQuestion(aView, question)
      }
    }
  }
}
