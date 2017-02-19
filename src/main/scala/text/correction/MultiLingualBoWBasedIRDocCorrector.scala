package text.correction

import jeqa.types.{BoWQuery, Geography}
import m17n.MultiLingual
import org.apache.uima.jcas.JCas

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualBoWBasedIRDocCorrector extends Corrector with MultiLingual {
  def correct(aJCas: JCas,
              query: BoWQuery,
              beginTimeLimit: Option[Int],
              endTimeLimit: Option[Int],
              geographyLimit: Option[Geography]): Unit = {

    correct(
      aJCas,
      query.getIndriQuery,
      Map.empty[String, Seq[String]],
      beginTimeLimit,
      endTimeLimit,
      geographyLimit,
      isBoWQuery = true
    )
    query.setAlreadyFinishedCorrecting(true)
  }
}
