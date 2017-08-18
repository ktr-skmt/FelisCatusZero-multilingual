package modules.text.vector.wordembedding.fastText.ja

import modules.text.vector.wordembedding.fastText.MultiLingualFastTextVectorGenerator
import modules.util.ModulesConfig
import us.feliscat.ir.fulltext.indri.IndriResult
import us.feliscat.ir.fulltext.indri.ja.JapaneseTrecText
import us.feliscat.m17n.Japanese
import us.feliscat.sentence.ja.JapaneseSentenceSplitter
import us.feliscat.text.StringOption
import us.feliscat.text.analyzer.mor.mecab.UnidicMecab
import us.feliscat.util.LibrariesConfig

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * @author K. Sakamoto
  *         Created on 2017/07/13
  */
object JapaneseFastTextVectorGenerator extends MultiLingualFastTextVectorGenerator with Japanese {
  override protected val fastTextResource: String = LibrariesConfig.fastTextJapaneseResource
  override protected val fastTextModel: String = LibrariesConfig.fastTextJapaneseModel
  override protected val trecTextFormatData: Seq[String] = ModulesConfig.trecTextFormatDataInJapanese

  override protected def toIndriResultMap(lines: Iterator[String],
                                keywordOriginalTextOpt: StringOption,
                                expansionOnlyList: Seq[String],
                                indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    JapaneseTrecText.toIndriResultMap(
      lines,
      keywordOriginalTextOpt,
      expansionOnlyList,
      indriResultMap)
  }

  override protected def extractWords(sentences: StringOption): Seq[String] = {
    val buffer = ListBuffer.empty[String]
    for (sentenceTmp <- JapaneseSentenceSplitter.split(sentences)) {
      UnidicMecab.extractWords(StringOption(sentenceTmp.text)) foreach {
        case word: String if !Set[String](" ", "　").contains(word) =>
          buffer += word
        case _ =>
          // Do nothing
      }
    }
    buffer.result
  }
}
