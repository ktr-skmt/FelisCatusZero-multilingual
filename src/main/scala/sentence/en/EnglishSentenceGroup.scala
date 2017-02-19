package sentence.en

import exam.en.EnglishLengthCounter
import jeqa.types.{Keyword, Sentence}
import m17n.English
import sentence.MultiLingualSentenceGroup
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/10.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishSentenceGroup(override val keyword: Keyword,
                           override val sentences: Seq[Sentence]) extends MultiLingualSentenceGroup(keyword, sentences) with English {
  override protected def count(sentenceOpt: StringOption): Int = {
    EnglishLengthCounter.count(sentenceOpt)
  }
}
