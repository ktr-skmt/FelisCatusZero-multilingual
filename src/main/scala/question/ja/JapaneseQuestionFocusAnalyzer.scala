package question.ja

import jeqa.types.Sentence
import m17n.Japanese
import question.MultiLingualQuestionFocusAnalyzer

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseQuestionFocusAnalyzer extends MultiLingualQuestionFocusAnalyzer with Japanese {
  override def analyze(sentenceSet: Seq[Sentence]): Seq[String] = {
    Nil
  }
}
