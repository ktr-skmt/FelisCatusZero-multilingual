package us.feliscat.answer.en

import us.feliscat.answer.{MultiLingualAnswerCandidate, MultiLingualAnswerCandidates}
import us.feliscat.m17n.English

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
