package us.feliscat.sentence.en

import us.feliscat.m17n.English
import us.feliscat.sentence.MultiLingualSentenceSplitter
import us.feliscat.text.StringOption
import us.feliscat.text.analyzer.CoreNLP4English

/**
  * <pre>
  * Created on 2017/02/16.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishSentenceSplitter extends MultiLingualSentenceSplitter with English {
  def split(textOpt: StringOption): Seq[String] = {
    CoreNLP4English.ssplit(textOpt)
  }
}
