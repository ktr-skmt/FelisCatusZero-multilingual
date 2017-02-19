package converter.en

import converter.{MultiLingualNgramSegmentator, MultiLingualUnigramSegmentator}
import m17n.English

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
