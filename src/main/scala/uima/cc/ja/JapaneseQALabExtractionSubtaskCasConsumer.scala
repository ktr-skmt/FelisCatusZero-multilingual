package uima.cc.ja

import m17n.Japanese
import sentence.ja.JapaneseSentenceSplitter
import text.StringOption
import uima.cc.MultiLingualQALabExtractionSubtask

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
