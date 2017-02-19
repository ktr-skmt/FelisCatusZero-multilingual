package exam.ja

import exam.MultiLingualLengthCounter
import m17n.Japanese
import text.{StringNone, StringOption, StringSome}

import scala.util.matching.Regex.Match

/**
  * <pre>
  * Created on 2017/02/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseLengthCounter extends MultiLingualLengthCounter with Japanese {
  override def count(text: StringOption): Int = {
    text.codePointCount - surplusForNumberSequence(text)
  }

  private def surplusForNumberSequence(text: StringOption): Int = {
    text match {
      case StringSome(t) =>
        var count: Int = 0
        "[0-9]{2,}".r.findAllMatchIn(t) foreach {
          m: Match =>
            count += m.group(0).length
        }
        (count + 1) / 2
      case StringNone => 0
    }
  }
}
