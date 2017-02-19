package sentence

import m17n.MultiLingual
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualSentenceCombinationGenerator(scoreIndex: Int) extends MultiLingual {
  protected var endingLimit: Int = 0
  protected def count(textOpt: StringOption): Int
}
