package time

import m17n.MultiLingual
import text.StringOption

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
