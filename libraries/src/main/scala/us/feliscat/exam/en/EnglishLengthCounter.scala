package us.feliscat.exam.en

import us.feliscat.exam.MultiLingualLengthCounter
import us.feliscat.m17n.English
import us.feliscat.text.StringOption

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
