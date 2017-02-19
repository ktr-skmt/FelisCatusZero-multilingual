package text.term

import m17n.MultiLingual
import text.StringOption

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