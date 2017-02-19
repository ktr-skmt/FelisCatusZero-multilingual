package exam.en

import exam.MultiLingualLengthCounter
import m17n.English
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishLengthCounter extends MultiLingualLengthCounter with English {
  override def count(text: StringOption): Int = {
    if (text.isEmpty) {
      0
    } else {
      text.get.split(' ').length
    }
  }
}
