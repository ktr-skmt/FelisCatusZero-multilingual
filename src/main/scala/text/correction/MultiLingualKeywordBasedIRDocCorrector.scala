package text.correction

import jeqa.types.{Geography, KeywordQuery}
import m17n.MultiLingual
import org.apache.uima.jcas.JCas

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualKeywordBasedIRDocCorrector extends Corrector with MultiLingual {
  def correct(aJCas: JCas,
              query: KeywordQuery,
              keywordCorrectionMap: Map[String, Seq[String]],
              beginTimeLimit: Option[Int],
              endTimeLimit: Option[Int],
              geographyLimit: Option[Geography]): Unit = {

    correct(
      aJCas,
      query.getKeyword,
      keywordCorrectionMap,
      beginTimeLimit,
      endTimeLimit,
      geographyLimit,
      isBoWQuery = false
    )
    query.setAlreadyFinishedCorrecting(true)
  }
}
