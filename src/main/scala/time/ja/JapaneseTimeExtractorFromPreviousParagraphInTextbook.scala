package time.ja

import ir.fulltext.indri.IndriResult
import ir.fulltext.indri.ja.JapaneseTrecText
import m17n.Japanese
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
object JapaneseTimeExtractorFromPreviousParagraphInTextbook
  extends MultiLingualTimeExtractorFromPreviousParagraphInTextbook
    with Japanese {
  override protected val trecTextFormatData: Seq[String] = Config.trecTextFormatDataInJapanese

  override protected def extractForWorldHistory(sentenceOpt: StringOption): Seq[TimeTmp] =
    JapaneseTimeExtractorForWorldHistory.extract(sentenceOpt)

  override protected def toIndriResultMap(lines: Iterator[String],
                                          keywordOriginalTextOpt: StringOption,
                                          expansionOnlyList: Seq[String],
                                          indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    JapaneseTrecText.toIndriResultMap(lines, keywordOriginalTextOpt, expansionOnlyList, indriResultMap)
  }
}
