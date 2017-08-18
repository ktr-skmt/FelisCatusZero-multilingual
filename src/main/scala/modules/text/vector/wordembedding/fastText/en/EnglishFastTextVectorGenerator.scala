package modules.text.vector.wordembedding.fastText.en

import modules.text.vector.wordembedding.fastText.MultiLingualFastTextVectorGenerator
import modules.util.ModulesConfig
import us.feliscat.ir.fulltext.indri.IndriResult
import us.feliscat.ir.fulltext.indri.en.EnglishTrecText
import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.util.LibrariesConfig

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * @author K. Sakamoto
  *         Created on 2017/07/13
  */
object EnglishFastTextVectorGenerator extends MultiLingualFastTextVectorGenerator with English {
  override protected val fastTextResource: String = LibrariesConfig.fastTextEnglishResource
  override protected val fastTextModel: String = LibrariesConfig.fastTextEnglishModel
  override protected val trecTextFormatData: Seq[String] = ModulesConfig.trecTextFormatDataInEnglish

  override protected def toIndriResultMap(lines: Iterator[String],
                                keywordOriginalTextOpt: StringOption,
                                expansionOnlyList: Seq[String],
                                indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    EnglishTrecText.toIndriResultMap(
      lines,
      keywordOriginalTextOpt,
      expansionOnlyList,
      indriResultMap)
  }

  override protected def extractWords(sentences: StringOption): Seq[String] = {
    val buffer = ListBuffer.empty[String]
    sentences foreach {
      s: String =>
        s.split(' ') foreach {
          word: String =>
            buffer += word
        }
    }
    buffer.result
  }
}
