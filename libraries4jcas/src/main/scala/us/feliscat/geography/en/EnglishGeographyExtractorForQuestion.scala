package us.feliscat.geography.en

import us.feliscat.geography.MultiLingualGeographyExtractorForQuestion
import us.feliscat.m17n.English
import us.feliscat.types.Sentence

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
