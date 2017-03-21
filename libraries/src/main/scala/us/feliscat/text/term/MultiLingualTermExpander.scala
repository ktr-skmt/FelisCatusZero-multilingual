package us.feliscat.text.term

import us.feliscat.m17n.MultiLingual
import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualTermExpander extends MultiLingual {
  def expand(termOpt: StringOption): Seq[(String, Boolean)]
}