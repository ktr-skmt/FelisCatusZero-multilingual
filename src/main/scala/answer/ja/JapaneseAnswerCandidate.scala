package answer.ja

import answer.MultiLingualAnswerCandidate
import exam.ja.JapaneseLengthCounter
import jeqa.types.Sentence
import m17n.Japanese
import text.StringOption

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
