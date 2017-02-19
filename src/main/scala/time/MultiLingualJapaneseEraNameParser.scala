package time

import m17n.MultiLingual
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualJapaneseEraNameParser extends MultiLingual {
  def convertToRomanCalendar(text: StringOption): StringOption
}
