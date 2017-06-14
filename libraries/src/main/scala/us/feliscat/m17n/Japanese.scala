package us.feliscat.m17n

import java.util.Locale

import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.ja.JapaneseNormalizedString

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait Japanese extends MultiLingual {
  override lazy val locale: Locale = Locale.JAPANESE

  override def normalize(textOpt: StringOption): StringOption = {
    JapaneseNormalizedString(textOpt).toStringOption
  }
}
