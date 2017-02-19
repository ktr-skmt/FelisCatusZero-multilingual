package question.en

import m17n.English
import question.MultiLingualQueryGenerator
import text.analyzer.SentenceQuotationParser
import text.term.en.EnglishTermExpander
import text.{StringNone, StringOption, StringSome}

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishQueryGenerator extends MultiLingualQueryGenerator with English {
  override protected def expand(keywordOpt: StringOption): Seq[(String, Boolean)] = {
    val expansionBuffer = ListBuffer.empty[(String, Boolean)]
    keywordOpt match {
      case StringSome(keyword) =>
        expansionBuffer += ((keyword, true))
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

        expansionBuffer ++= EnglishTermExpander.expand(keywordOpt)
      case StringNone =>
        // Do nothing
    }
    expansionBuffer.result.distinct
  }
}
