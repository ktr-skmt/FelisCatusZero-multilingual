package question.en

import jeqa.types.Sentence
import m17n.English
import question.MultiLingualQuestionFocusAnalyzer

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishQuestionFocusAnalyzer extends MultiLingualQuestionFocusAnalyzer with English {
  override def analyze(sentenceSet: Seq[Sentence]): Seq[String] = {
    Nil
  }
}
