package geography

import jeqa.types.Sentence
import m17n.MultiLingual

/**
  * <pre>
  * Created on 2017/02/10.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualGeographyExtractorForQuestion extends MultiLingual {
  def extract(sentenceList: Seq[Sentence]): (Seq[String], Seq[String])
}
