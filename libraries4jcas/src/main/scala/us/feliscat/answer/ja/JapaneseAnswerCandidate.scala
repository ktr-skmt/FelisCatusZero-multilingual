package us.feliscat.answer.ja

import us.feliscat.answer.MultiLingualAnswerCandidate
import us.feliscat.exam.ja.JapaneseLengthCounter
import us.feliscat.m17n.Japanese
import us.feliscat.text.StringOption
import us.feliscat.types.Sentence

/**
  * <pre>
  * Created on 2017/02/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseAnswerCandidate(override val score: Double,
                              override val text: StringOption,
                              override val sentenceList: Seq[Sentence])
  extends MultiLingualAnswerCandidate(score, text, sentenceList) with Japanese {
  override val length: Int = JapaneseLengthCounter.count(text)
}
