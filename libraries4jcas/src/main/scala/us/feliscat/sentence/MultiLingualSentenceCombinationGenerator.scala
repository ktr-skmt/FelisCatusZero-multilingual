package us.feliscat.sentence

import us.feliscat.m17n.MultiLingual
import us.feliscat.text.StringOption

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
