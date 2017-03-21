package us.feliscat.converter.ja

import us.feliscat.converter.{MultiLingualNgramSegmentator, MultiLingualUnigramSegmentator}
import us.feliscat.m17n.Japanese

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseUnigramSegmentator extends MultiLingualUnigramSegmentator with Japanese {
  override val segmentator: MultiLingualNgramSegmentator = new JapaneseNgramSegmentator(1)
}
