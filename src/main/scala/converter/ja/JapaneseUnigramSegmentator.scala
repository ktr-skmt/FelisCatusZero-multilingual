package converter.ja

import converter.{MultiLingualNgramSegmentator, MultiLingualUnigramSegmentator}
import m17n.Japanese

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
