package ir.fulltext.indri.ja

import ir.fulltext.indri.{IndriResult, MultiLingualRetrievalByBoW}
import m17n.Japanese
import question.ja.JapaneseKnowledgeSourceSelector
import text.StringOption
import util.Config

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
  override protected val trecTextFormatData: Seq[String] = Config.trecTextFormatDataInJapanese

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
