package uima.ae.ir.ja

import java.util.Locale

import org.apache.uima.jcas.JCas
import uima.ae.ir.MultiLingualInformationRetriever
import uima.modules.common.ja.JapaneseDocumentAnnotator
import uima.modules.ir.correction.ja.{JapaneseBoWBasedIRDocCorrector, JapaneseKeywordBasedIRDocCorrector}
import uima.modules.ir.fulltext.indri.ja.{JapaneseRetrievalByBoW, JapaneseRetrievalByKeyword}
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
object JapaneseInformationRetriever extends MultiLingualInformationRetriever with JapaneseDocumentAnnotator {
  override protected def retrieveByKeyword(aJCas: JCas,
                                           query: KeywordQuery,
                                           keywordCorrectionMap: mutable.Map[String, Seq[String]])(implicit id: JCasID): Option[Long] = {
    if (localeId != Locale.JAPANESE.getLanguage) {
      return None
    }
    Option(
      JapaneseRetrievalByKeyword.retrieve(
        aJCas,
        query,
        keywordCorrectionMap,
        mIndriScoreIndex,
        mDocumentId
      )
    )
  }

  override protected def retrieveByBoW(aJCas: JCas, query: BoWQuery)(implicit id: JCasID): Option[Long] = {
    if (localeId != Locale.JAPANESE.getLanguage) {
      return None
    }
    Option(
      JapaneseRetrievalByBoW.retrieve(
        aJCas,
        query,
        mIndriScoreIndex,
        mDocumentId
      )
    )
  }

  override protected def correctDocByKeyword(aJCas: JCas,
                                    query: KeywordQuery,
                                    keywordCorrectionMap: Map[String, Seq[String]],
                                    beginTimeLimit: Option[Int],
                                    endTimeLimit: Option[Int],
                                    geographyLimit: Option[Geography])(implicit id: JCasID): Unit = {
    JapaneseKeywordBasedIRDocCorrector.correct(
      aJCas,
      query,
      keywordCorrectionMap,
      beginTimeLimit,
      endTimeLimit,
      geographyLimit
    )
  }

  override protected def correctDocByBoW(aJCas: JCas,
                                query: BoWQuery,
                                beginTimeLimit: Option[Int],
                                endTimeLimit: Option[Int],
                                geographyLimit: Option[Geography])(implicit id: JCasID): Unit = {
    JapaneseBoWBasedIRDocCorrector.correct(
      aJCas,
      query,
      beginTimeLimit,
      endTimeLimit,
      geographyLimit
    )
  }
}
