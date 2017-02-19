package geography.ja

import geography.MultiLingualGeographyExtractorForQuestion
import jeqa.types.Sentence
import m17n.Japanese

/**
  * <pre>
  * Created on 2017/02/10.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseGeographyExtractorForQuestion extends MultiLingualGeographyExtractorForQuestion with Japanese {
  override def extract(sentenceList: Seq[Sentence]): (Seq[String], Seq[String]) = {
    (Nil, Nil)
  }
}
