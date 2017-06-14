package us.feliscat.m17n

import java.util.Locale

import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingual {
  val locale: Locale

  protected val localeId: String = locale.getLanguage

  protected def normalize(textOpt: StringOption): StringOption
}
