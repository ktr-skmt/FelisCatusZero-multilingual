package us.feliscat.m17n

import java.util.Locale

import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.en.EnglishNormalizedString

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait English extends MultiLingual {
  override lazy val locale: Locale = Locale.ENGLISH

  override def normalize(textOpt: StringOption): StringOption = {
    EnglishNormalizedString(textOpt).toStringOption
  }
}
