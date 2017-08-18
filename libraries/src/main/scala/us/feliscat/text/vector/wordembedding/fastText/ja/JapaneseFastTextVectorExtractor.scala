package us.feliscat.text.vector.wordembedding.fastText.ja

import us.feliscat.m17n.Japanese
import us.feliscat.text.vector.wordembedding.fastText.MultiLingualFastTextVectorExtractor
import us.feliscat.util.LibrariesConfig

/**
  * @author K. Sakamoto
  *         Created on 2017/07/13
  */
object JapaneseFastTextVectorExtractor extends MultiLingualFastTextVectorExtractor with Japanese {
  override protected val fastTextQuery: String = LibrariesConfig.fastTextJapaneseQuery
  override protected val fastTextModelBin: String = LibrariesConfig.fastTextJapaneseModelBin
}
