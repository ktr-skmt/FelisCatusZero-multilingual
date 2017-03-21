package uima.cr.en

import us.feliscat.m17n.English
import uima.cr.MultiLingualQuestionReader

import scala.util.matching.Regex

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishQuestionReader extends MultiLingualQuestionReader with English {
  protected override val lengthLimitBeginRegex: Regex = """no less than ([0-9]+) (?:English )?words""".r
  protected override val lengthLimitEndRegex:   Regex = """no more than ([0-9]+) (?:English )?words""".r
  protected override val lengthLimitApproximatorRegex: Regex = """(?:about|approximately) ([0-9]+) (?:English )?words""".r
  protected override val concisenessSet: Set[String] = Set[String](
    "concisely",
    "briefly",
    "in brief",
    "shortly",
    "in short"
  )
}
