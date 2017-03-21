package us.feliscat.answer

import us.feliscat.m17n.MultiLingual
import us.feliscat.text.StringOption
import us.feliscat.types.Sentence

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
