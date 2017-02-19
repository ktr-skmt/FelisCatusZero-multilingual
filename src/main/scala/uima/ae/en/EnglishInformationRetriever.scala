package uima.ae.en

import java.util.Locale

import ir.fulltext.indri.en.{EnglishRetrievalByBoW, EnglishRetrievalByKeyword}
import jeqa.types.{BoWQuery, Geography, KeywordQuery}
import org.apache.uima.jcas.JCas
import text.correction.en.{EnglishBoWBasedIRDocCorrector, EnglishKeywordBasedIRDocCorrector}
import uima.ae.MultiLingualInformationRetriever

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishInformationRetriever extends MultiLingualInformationRetriever with EnglishDocumentAnnotator {
  override protected def retrieveByKeyword(aJCas: JCas,
                                  query: KeywordQuery,
                                  keywordCorrectionMap: mutable.Map[String, Seq[String]]): Option[Long] = {
    if (localeId != Locale.ENGLISH.getLanguage) {
      return None
    }
    Option(EnglishRetrievalByKeyword.retrieve(
      aJCas,
      query,
      keywordCorrectionMap,
      mIndriScoreIndex,
      mDocumentId))
  }


  override protected def retrieveByBoW(aJCas: JCas, query: BoWQuery): Option[Long] = {
    if (localeId != Locale.ENGLISH.getLanguage) {
      return None
    }
    Option(EnglishRetrievalByBoW.retrieve(
      aJCas,
      query,
      mIndriScoreIndex,
      mDocumentId))
  }

  override protected def correctDocByKeyword(aJCas: JCas,
                                             query: KeywordQuery,
                                             keywordCorrectionMap: Map[String, Seq[String]],
                                             beginTimeLimit: Option[Int],
                                             endTimeLimit: Option[Int],
                                             geographyLimit: Option[Geography]): Unit = {
    EnglishKeywordBasedIRDocCorrector.correct(
      aJCas,
      query,
      keywordCorrectionMap,
      beginTimeLimit,
      endTimeLimit,
      geographyLimit)
  }

  override protected def correctDocByBoW(aJCas: JCas,
                                         query: BoWQuery,
                                         beginTimeLimit: Option[Int],
                                         endTimeLimit: Option[Int],
                                         geographyLimit: Option[Geography]): Unit = {
    EnglishBoWBasedIRDocCorrector.correct(
      aJCas,
      query,
      beginTimeLimit,
      endTimeLimit,
      geographyLimit)
  }
}
