package answer.en

import answer.{MultiLingualAnswerCandidate, MultiLingualAnswerCandidates}
import m17n.English

/**
  * <pre>
  * Created on 2017/02/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishAnswerCandidates (override val answerResults: Seq[MultiLingualAnswerCandidate])
  extends MultiLingualAnswerCandidates(answerResults) with English {

}
