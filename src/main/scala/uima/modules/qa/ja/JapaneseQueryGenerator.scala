package uima.modules.qa.ja

import uima.modules.qa.MultiLingualQueryGenerator
import us.feliscat.m17n.Japanese
import us.feliscat.text.analyzer.SentenceQuotationParser
import us.feliscat.text.term.ja.JapaneseTermExpander
import us.feliscat.text.{StringNone, StringOption, StringSome}

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseQueryGenerator extends MultiLingualQueryGenerator with Japanese {
  //private lazy val KATAKANA_KANJI: Regex = raw"""(\p{InKatakana}+)(${JISLevel1to4KanjiCharacter.pattern}+)""".r
  private lazy val KATAKANA_KANJI: Regex = raw"""(\p{script=Katakana}+)(\p{script=Han}+)""".r
  private lazy val DELIMITER_LIST: Seq[String] =
    "\u003D" :: //=
    "\uFF65" :: //・
    "\uFE66" :: //＝
    Nil
  private lazy val DELIMITER_REGEX: String = DELIMITER_LIST.mkString("[", "", "]")
  private lazy val PARTICLE_LIST: Seq[String] =
    "の" ::
    "が" ::
    "を" ::
    "に" ::
    "で" ::
    Nil

  override protected def expand(keywordOpt: StringOption): Seq[(String, Boolean)] = {
    val expansionBuffer = ListBuffer.empty[(String, Boolean)]
    keywordOpt match {
      case StringSome(keyword) =>
        expansionBuffer += ((keyword, true))
        keyword match {
          case KATAKANA_KANJI(katakana, kanji) =>
            expansionBuffer += ((katakana, false))
            PARTICLE_LIST foreach {
              particle: String =>
                expansionBuffer += ((s"""$katakana$particle$kanji""", false))
            }
          case _ =>
          // Do nothing
        }
        DELIMITER_LIST foreach {
          delimiter: String =>
            val tmp: String = keyword.replaceAll(DELIMITER_REGEX, delimiter)
            if (keyword != tmp) {
              expansionBuffer += ((tmp, true))
            }
        }
        var keywordTmp: String = keyword
        var quotedSequenceOpt: Option[(Quotation, Range)] = SentenceQuotationParser.getFirstMatchOpt(StringOption(keywordTmp))
        while (quotedSequenceOpt.nonEmpty) {
          val quotedSequence: (Quotation, Range) = quotedSequenceOpt.get
          val range: Range = quotedSequence._2
          expansionBuffer += ((keywordTmp.substring(range.start + 1, range.end - 1), true))
          keywordTmp = keywordTmp.substring(0, range.start) concat keywordTmp.substring(range.end, keywordTmp.length)
          quotedSequenceOpt = SentenceQuotationParser.getFirstMatchOpt(StringOption(keywordTmp))
        }
        expansionBuffer += ((keywordTmp, true))
        expansionBuffer ++= JapaneseTermExpander.expand(keywordOpt)
      case StringNone =>
      // Do nothing
    }
    expansionBuffer.result.distinct
  }
}
