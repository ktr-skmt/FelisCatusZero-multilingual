package us.feliscat.answer.en

import us.feliscat.answer.MultiLingualAnswerCandidate
import us.feliscat.exam.en.EnglishLengthCounter
import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.types.Sentence

/**
  * <pre>
  * Created on 2017/02/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishAnswerCandidate(override val score: Double,
                             override val text: StringOption,
                             override val sentenceList: Seq[Sentence])
  extends MultiLingualAnswerCandidate(score, text, sentenceList) with English {
  override val length: Int = EnglishLengthCounter.count(text)
}
