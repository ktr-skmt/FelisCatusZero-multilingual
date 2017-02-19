package ir.query

import converter.ja.{JapaneseNgramSegmentator, JapaneseUnigramSegmentator}
import text.StringOption

/**
 * @param keyword keyword
 * @author K.Sakamoto
 *         Created on 15/10/28
 */
class Keyword(val keyword: StringOption) extends And with Or with Command {
  override protected val command: String = "#1(%s)"
  override def toString: String = {
    characterUnigram(keyword).getOrElse("")
  }

  private def characterUnigram(keyword: StringOption): StringOption = {
    JapaneseUnigramSegmentator.segmentator.asInstanceOf[JapaneseNgramSegmentator].
      segmentateWithCharacter(keyword)
  }
}

/**
  * @author K.sakamoto
  * @param list OR list
  */
class AndList(override val list: Seq[Or]) extends AndOrList[Or](list) with And {
  override protected val command: String = "#band(%s)"
}

/**
  * @author K.Sakamoto
  * @param list AND list
  */
class OrList(override val list: Seq[And]) extends AndOrList[And](list) with Or {
  override protected val command: String = "#combine(%s)"
}

/**
  * @author K.Sakamoto
  */
trait And extends AndOr

/**
  * @author K.Sakamoto
  */
trait Or extends AndOr

/**
  * @author K.Sakamoto
  */
trait AndOr

/**
  * @author K.Sakamoto
  * @param list list
  * @tparam A type
  */
abstract class AndOrList[A <: AndOr](val list: Seq[A]) extends Command {
  override def toString: String = {
    val builder = new StringBuilder()
    list foreach {
      element: A =>
        builder.
          append('\u0020').//" "
          append(element)
    }
    command.format(builder.deleteCharAt(0).result)
  }
}

/**
  * @author K.Sakamoto
  */
trait Command {
  protected val command: String
}