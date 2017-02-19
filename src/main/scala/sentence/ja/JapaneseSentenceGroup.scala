package sentence.ja

import exam.ja.JapaneseLengthCounter
import jeqa.types.{Keyword, Sentence}
import m17n.Japanese
import sentence.MultiLingualSentenceGroup
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/10.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseSentenceGroup(override val keyword: Keyword,
                            override val sentences: Seq[Sentence])
  extends MultiLingualSentenceGroup(keyword, sentences) with Japanese {
  override protected def count(sentenceOpt: StringOption): Int = {
    JapaneseLengthCounter.count(sentenceOpt)
  }
}
