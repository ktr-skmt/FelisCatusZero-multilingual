package sentence.en

import m17n.English
import sentence.MultiLingualSentenceSplitter
import text.StringOption
import text.analyzer.CoreNLP4English

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
