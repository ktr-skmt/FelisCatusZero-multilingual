package ir.fulltext.indri.en

import ir.fulltext.indri.{IndriResult, MultiLingualRetrievalByBoW}
import m17n.English
import question.en.EnglishKnowledgeSourceSelector
import text.StringOption
import util.Config

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishRetrievalByBoW extends MultiLingualRetrievalByBoW with English {
  override protected val trecTextFormatData: Seq[String] = Config.trecTextFormatDataInEnglish

  override protected def selectKnowledgeSource(isKeywordQuery: Boolean): Seq[String] = {
    EnglishKnowledgeSourceSelector.select(isKeywordQuery)
  }

  override protected def toIndriResultMap(lines: Iterator[String],
                                          keywordOriginalTextOpt: StringOption,
                                          expansionOnlyList: Seq[String],
                                          indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    EnglishTrecText.toIndriResultMap(lines, keywordOriginalTextOpt, expansionOnlyList, indriResultMap)
  }
}
