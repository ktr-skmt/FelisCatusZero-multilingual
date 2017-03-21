package us.feliscat.sentence.ja

import us.feliscat.exam.ja.JapaneseLengthCounter
import us.feliscat.m17n.Japanese
import us.feliscat.sentence.MultiLingualSentenceGroup
import us.feliscat.text.StringOption
import us.feliscat.types.{Keyword, Sentence}

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
