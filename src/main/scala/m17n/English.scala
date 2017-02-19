package m17n

import java.util.Locale

import text.StringOption
import text.normalizer.en.EnglishNormalizedString

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait English extends MultiLingual {
  override protected val localeId: String = Locale.ENGLISH.getLanguage

  override def normalize(textOpt: StringOption): StringOption = {
    EnglishNormalizedString(textOpt).toStringOption
  }
}
