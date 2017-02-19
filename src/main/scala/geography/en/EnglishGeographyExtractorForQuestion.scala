package geography.en

import geography.MultiLingualGeographyExtractorForQuestion
import jeqa.types.Sentence
import m17n.English

/**
  * <pre>
  * Created on 2017/02/10.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishGeographyExtractorForQuestion extends MultiLingualGeographyExtractorForQuestion with English {
  override def extract(sentenceList: Seq[Sentence]): (Seq[String], Seq[String]) = {
    (Nil, Nil)
  }
}
