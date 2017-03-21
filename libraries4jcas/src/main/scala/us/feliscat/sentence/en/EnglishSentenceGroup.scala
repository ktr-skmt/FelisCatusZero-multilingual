package us.feliscat.sentence.en

import us.feliscat.exam.en.EnglishLengthCounter
import us.feliscat.m17n.English
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
class EnglishSentenceGroup(override val keyword: Keyword,
                           override val sentences: Seq[Sentence]) extends MultiLingualSentenceGroup(keyword, sentences) with English {
  override protected def count(sentenceOpt: StringOption): Int = {
    EnglishLengthCounter.count(sentenceOpt)
  }
}
