package us.feliscat.m17n

import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.en.EnglishNormalizedString

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait English extends MultiLingual with EnglishLocale {
  override def normalize(textOpt: StringOption): StringOption = {
    EnglishNormalizedString(textOpt).toStringOption
  }
}
