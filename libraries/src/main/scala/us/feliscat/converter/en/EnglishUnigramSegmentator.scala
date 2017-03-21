package us.feliscat.converter.en

import us.feliscat.converter.{MultiLingualNgramSegmentator, MultiLingualUnigramSegmentator}
import us.feliscat.m17n.English

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishUnigramSegmentator extends MultiLingualUnigramSegmentator with English {
  override val segmentator: MultiLingualNgramSegmentator = new EnglishNgramSegmentator(1)
}
