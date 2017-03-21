package us.feliscat.converter

import us.feliscat.m17n.MultiLingual

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualUnigramSegmentator extends MultiLingual {
  val segmentator: MultiLingualNgramSegmentator
}
