package uima.cc.en

import m17n.English
import sentence.en.EnglishSentenceSplitter
import text.StringOption
import uima.cc.MultiLingualQALabExtractionSubtask

/**
  * <pre>
  * Created on 2017/02/18.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait EnglishQALabExtractionSubtaskCasConsumer
  extends MultiLingualQALabExtractionSubtask with English {
  override protected def reviseText(docno: String, title: String, text: String): String = {
    val term: String = title.split(',').head

    if (isGlossary(docno)) {
      val builder = new StringBuilder()
      EnglishSentenceSplitter.split(StringOption(text.trim)) foreach {
        sentence: String =>
          builder.
            append(term).
            append(" is ").
            append(sentence.trim)
      }
      builder.result
    } else {
      text.trim
    }
  }
}
