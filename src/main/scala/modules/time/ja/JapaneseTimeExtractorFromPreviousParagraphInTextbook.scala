package modules.time.ja

import modules.time.MultiLingualTimeExtractorFromPreviousParagraphInTextbook
import modules.util.ModulesConfig
import us.feliscat.ir.fulltext.indri.IndriResult
import us.feliscat.ir.fulltext.indri.ja.JapaneseTrecText
import us.feliscat.m17n.Japanese
import us.feliscat.text.StringOption
import us.feliscat.time.ja.JapaneseTimeExtractorForWorldHistory
import us.feliscat.time.TimeTmp

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
  override protected val trecTextFormatData: Seq[String] = ModulesConfig.trecTextFormatDataInJapanese

  override protected def extractForWorldHistory(sentenceOpt: StringOption): Seq[TimeTmp] =
    JapaneseTimeExtractorForWorldHistory.extract(sentenceOpt)

  override protected def toIndriResultMap(lines: Iterator[String],
                                          keywordOriginalTextOpt: StringOption,
                                          expansionOnlyList: Seq[String],
                                          indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    JapaneseTrecText.toIndriResultMap(lines, keywordOriginalTextOpt, expansionOnlyList, indriResultMap)
  }
}
