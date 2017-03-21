package uima.cc.qalab.ja

import uima.cc.qalab.MultiLingualQALabExtractionSubtask
import us.feliscat.m17n.Japanese
import us.feliscat.sentence.ja.JapaneseSentenceSplitter
import us.feliscat.text.StringOption

import scala.util.matching.Regex

/**
  * <pre>
  * Created on 2017/02/18.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait JapaneseQALabExtractionSubtaskCasConsumer
  extends MultiLingualQALabExtractionSubtask with Japanese {
  private val regex: Regex = """(.+)-\w+""".r

  override protected def reviseText(docno: String, title: String, text: String): String = {
    val term: String = title.split(',').head match {
      case regex(t) =>
        t
      case otherwise =>
        otherwise
    }

    if (isGlossary(docno)) {
      val builder = new StringBuilder()
      JapaneseSentenceSplitter.split(StringOption(text)) foreach {
        normalizedSentence =>
          builder.
            append(term).
            append("は、").
            append(normalizedSentence.text)
      }
      builder.result
    } else {
      text
    }
  }
}
