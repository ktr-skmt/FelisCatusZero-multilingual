package time.en

import m17n.English
import text.StringOption
import time.MultiLingualJapaneseEraNameParser

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
