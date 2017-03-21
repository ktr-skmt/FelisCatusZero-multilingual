package modules.time.en

import modules.time.MultiLingualTimeExtractorFromPreviousParagraphInTextbook
import modules.util.ModulesConfig
import us.feliscat.ir.fulltext.indri.IndriResult
import us.feliscat.ir.fulltext.indri.en.EnglishTrecText
import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.time.en.EnglishTimeExtractorForWorldHistory
import us.feliscat.time.TimeTmp

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
  override protected val trecTextFormatData: Seq[String] = ModulesConfig.trecTextFormatDataInEnglish

  override protected def extractForWorldHistory(sentenceOpt: StringOption): Seq[TimeTmp] =
    EnglishTimeExtractorForWorldHistory.extract(sentenceOpt)

  override protected def toIndriResultMap(lines: Iterator[String],
                                          keywordOriginalTextOpt: StringOption,
                                          expansionOnlyList: Seq[String],
                                          indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    EnglishTrecText.toIndriResultMap(lines, keywordOriginalTextOpt, expansionOnlyList, indriResultMap)
  }
}
