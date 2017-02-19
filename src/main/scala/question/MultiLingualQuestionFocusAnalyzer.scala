package question

import jeqa.types.Sentence
import m17n.MultiLingual

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQuestionFocusAnalyzer extends MultiLingual {
  def analyze(sentenceSet: Seq[Sentence]): Seq[String]
}
