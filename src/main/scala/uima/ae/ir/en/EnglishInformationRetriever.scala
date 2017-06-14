package uima.ae.ir.en

import java.util.Locale

import org.apache.uima.jcas.JCas
import uima.ae.ir.MultiLingualInformationRetriever
import uima.modules.common.en.EnglishDocumentAnnotator
import uima.modules.ir.correction.en.{EnglishBoWBasedIRDocCorrector, EnglishKeywordBasedIRDocCorrector}
import uima.modules.ir.fulltext.indri.en.{EnglishRetrievalByBoW, EnglishRetrievalByKeyword}
import us.feliscat.types.{BoWQuery, Geography, KeywordQuery}
import us.feliscat.util.uima.JCasID

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
                                  keywordCorrectionMap: mutable.Map[String, Seq[String]])(implicit id: JCasID): Option[Long] = {
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


  override protected def retrieveByBoW(aJCas: JCas, query: BoWQuery)(implicit id: JCasID): Option[Long] = {
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
                                             geographyLimit: Option[Geography])(implicit id: JCasID): Unit = {
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
                                         geographyLimit: Option[Geography])(implicit id: JCasID): Unit = {
    EnglishBoWBasedIRDocCorrector.correct(
      aJCas,
      query,
      beginTimeLimit,
      endTimeLimit,
      geographyLimit)
  }
}
