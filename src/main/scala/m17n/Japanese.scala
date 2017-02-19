package m17n

import java.util.Locale

import text.StringOption
import text.normalizer.ja.JapaneseNormalizedString

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait Japanese extends MultiLingual {
  override protected val localeId: String = Locale.JAPANESE.getLanguage

  override def normalize(textOpt: StringOption): StringOption = {
    JapaneseNormalizedString(textOpt).toStringOption
  }
}
