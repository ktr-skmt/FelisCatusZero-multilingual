package us.feliscat.geography

import us.feliscat.m17n.MultiLingual
import us.feliscat.types.Sentence

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
