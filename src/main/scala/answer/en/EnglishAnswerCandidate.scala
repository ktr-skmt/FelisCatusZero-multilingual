package answer.en

import answer.MultiLingualAnswerCandidate
import exam.en.EnglishLengthCounter
import jeqa.types.Sentence
import m17n.English
import text.StringOption

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
