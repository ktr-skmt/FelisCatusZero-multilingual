package us.feliscat.m17n

import java.util.Locale

/**
  * @author K. Sakamoto
  *         Created on 2017/08/09
  */
trait JapaneseLocale extends MultiLingualLocale {
  override lazy val locale: Locale = Locale.JAPANESE
}
