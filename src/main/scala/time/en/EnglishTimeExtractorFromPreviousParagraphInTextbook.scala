package time.en

import ir.fulltext.indri.IndriResult
import ir.fulltext.indri.en.EnglishTrecText
import m17n.English
import text.StringOption
import time.{MultiLingualTimeExtractorFromPreviousParagraphInTextbook, TimeTmp}
import util.Config

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTimeExtractorFromPreviousParagraphInTextbook
  extends MultiLingualTimeExtractorFromPreviousParagraphInTextbook
    with English {
  override protected val trecTextFormatData: Seq[String] = Config.trecTextFormatDataInEnglish

  override protected def extractForWorldHistory(sentenceOpt: StringOption): Seq[TimeTmp] =
    EnglishTimeExtractorForWorldHistory.extract(sentenceOpt)

  override protected def toIndriResultMap(lines: Iterator[String],
                                          keywordOriginalTextOpt: StringOption,
                                          expansionOnlyList: Seq[String],
                                          indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    EnglishTrecText.toIndriResultMap(lines, keywordOriginalTextOpt, expansionOnlyList, indriResultMap)
  }
}
