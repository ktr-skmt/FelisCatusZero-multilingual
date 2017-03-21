package us.feliscat.time

import us.feliscat.m17n.MultiLingual
import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualTimeExtractionPreprocessor extends MultiLingual {
  def convert(text: StringOption): StringOption
}
