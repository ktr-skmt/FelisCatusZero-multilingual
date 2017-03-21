package uima.modules.ir.fulltext.indri.ja

import modules.question.ja.JapaneseKnowledgeSourceSelector
import modules.util.ModulesConfig
import uima.modules.ir.fulltext.indri.MultiLingualRetrievalByBoW
import us.feliscat.ir.fulltext.indri.IndriResult
import us.feliscat.ir.fulltext.indri.ja.JapaneseTrecText
import us.feliscat.m17n.Japanese
import us.feliscat.text.StringOption

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/01/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseRetrievalByBoW
  extends MultiLingualRetrievalByBoW
    with Japanese {
  override protected val trecTextFormatData: Seq[String] = ModulesConfig.trecTextFormatDataInJapanese

  override protected def selectKnowledgeSource(isKeywordQuery: Boolean): Seq[String] = {
    JapaneseKnowledgeSourceSelector.select(isKeywordQuery)
  }

  override protected def toIndriResultMap(lines: Iterator[String],
                                          keywordOriginalTextOpt: StringOption,
                                          expansionOnlyList: Seq[String],
                                          indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    JapaneseTrecText.toIndriResultMap(lines, keywordOriginalTextOpt, expansionOnlyList, indriResultMap)
  }
}
