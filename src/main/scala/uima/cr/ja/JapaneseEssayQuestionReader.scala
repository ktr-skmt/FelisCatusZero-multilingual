package uima.cr.ja

import m17n.Japanese
import uima.cr.MultiLingualEssayQuestionReader

import scala.util.matching.Regex

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseEssayQuestionReader extends MultiLingualEssayQuestionReader with Japanese {
  protected override val lengthLimitBeginRegex: Regex = """([0-9]+)字以上""".r
  protected override val lengthLimitEndRegex:   Regex = """([0-9]+)字以[下内]""".r
  protected override val lengthLimitApproximatorRegex: Regex = """([0-9]+)字(?:程度|前後)""".r
  protected override val concisenessSet: Set[String] = Set[String](
    "簡潔",
    "短文"
  )
}
