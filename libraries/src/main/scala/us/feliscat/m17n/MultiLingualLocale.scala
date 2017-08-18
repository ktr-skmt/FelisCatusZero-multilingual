package us.feliscat.m17n

import java.util.Locale

/**
  * @author K. Sakamoto
  *         Created on 2017/08/09
  */
trait MultiLingualLocale {
  val locale: Locale
  protected val localeId: String = locale.getLanguage
}
