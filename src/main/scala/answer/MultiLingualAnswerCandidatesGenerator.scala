package answer

import m17n.MultiLingual
import sentence.MultiLingualSentenceCombination

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualAnswerCandidatesGenerator(scoreIndex: Int) extends MultiLingual {
  def generate(sentenceCombinationSet: Seq[MultiLingualSentenceCombination]): MultiLingualAnswerCandidates
}
