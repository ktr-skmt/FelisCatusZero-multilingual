package us.feliscat.m17n

import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.ja.JapaneseNormalizedString

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait Japanese extends MultiLingual with JapaneseLocale {
  override def normalize(textOpt: StringOption): StringOption = {
    JapaneseNormalizedString(textOpt).toStringOption
  }
}
