package answer

import jeqa.types.Sentence
import m17n.MultiLingual
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualAnswerCandidate(val score: Double,
                                           val text: StringOption,
                                           val sentenceList: Seq[Sentence]) extends MultiLingual {
  val length: Int
  override def toString: String = {
    """SCORE:
      |%f
      |TEXT:
      |%s
      |CHARACTER NUMBER:
      |%d
    """.stripMargin.format(
      score,
      text,
      length
    )
  }
}
