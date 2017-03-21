package us.feliscat.exam

import us.feliscat.m17n.MultiLingual
import us.feliscat.text.StringOption

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
