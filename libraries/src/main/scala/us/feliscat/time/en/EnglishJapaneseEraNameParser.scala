package us.feliscat.time.en

import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.time.MultiLingualJapaneseEraNameParser

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishJapaneseEraNameParser extends MultiLingualJapaneseEraNameParser with English {
  override def convertToRomanCalendar(text: StringOption): StringOption = StringOption.empty
}
