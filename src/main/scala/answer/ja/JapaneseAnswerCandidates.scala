package answer.ja

import answer.{MultiLingualAnswerCandidate, MultiLingualAnswerCandidates}
import m17n.Japanese

/**
  * <pre>
  * Created on 2017/02/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseAnswerCandidates(override val answerResults: Seq[MultiLingualAnswerCandidate])
  extends MultiLingualAnswerCandidates(answerResults) with Japanese {

}
