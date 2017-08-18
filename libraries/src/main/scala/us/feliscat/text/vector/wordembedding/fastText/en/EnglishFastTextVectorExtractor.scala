package us.feliscat.text.vector.wordembedding.fastText.en

import us.feliscat.m17n.English
import us.feliscat.text.vector.wordembedding.fastText.MultiLingualFastTextVectorExtractor
import us.feliscat.util.LibrariesConfig

/**
  * @author K. Sakamoto
  *         Created on 2017/07/13
  */
object EnglishFastTextVectorExtractor extends MultiLingualFastTextVectorExtractor with English {
  override protected val fastTextQuery: String = LibrariesConfig.fastTextEnglishQuery
  override protected val fastTextModelBin: String = LibrariesConfig.fastTextEnglishModelBin
}
