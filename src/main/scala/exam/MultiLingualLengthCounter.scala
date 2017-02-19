package exam

import m17n.MultiLingual
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualLengthCounter extends MultiLingual {
  def count(text: StringOption): Int
}
