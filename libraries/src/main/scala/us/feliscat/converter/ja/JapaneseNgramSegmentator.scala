package us.feliscat.converter.ja

import us.feliscat.converter.MultiLingualNgramSegmentator
import us.feliscat.m17n.Japanese
import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseNgramSegmentator(nGram: Int) extends MultiLingualNgramSegmentator(nGram) with Japanese {
  def segmentateWithSpaceChar(segments: Seq[String]): StringOption = {
    val builder = new StringBuilder()
    var isFirst: Boolean = true
    segments foreach {
      segment: String =>
        if (isFirst) {
          builder.
            append(segment)
          isFirst = false
        } else {
          builder.
            append(DELIMITER).
            append(segment)
        }
    }
    StringOption(builder.toString)
  }

  def segmentateWithCharacter(text: StringOption): StringOption = {
    merge(segmentate({
      import us.feliscat.util.primitive.StringUtils
      for (segment <- text.getOrElse("").toCodePointArray) yield {
        new String(Array[Int](segment), 0, nGram)
      }
    }))
  }
}
