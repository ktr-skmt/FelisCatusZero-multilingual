package answer

import m17n.MultiLingual

/**
  * <pre>
  * Created on 2017/02/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualAnswerCandidates(val answerResults: Seq[MultiLingualAnswerCandidate]) extends MultiLingual {
  override def toString: String = {
    val builder = new StringBuilder()
    answerResults foreach {
      result: MultiLingualAnswerCandidate =>
        builder.append(
          "%1$s%2$s---%2$s".format(
            result,
            "\n"
          )
        )
    }
    builder.result
  }
}
