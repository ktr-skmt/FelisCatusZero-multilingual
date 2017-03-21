package uima.modules.qa

import us.feliscat.m17n.MultiLingual
import us.feliscat.types.Sentence

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
