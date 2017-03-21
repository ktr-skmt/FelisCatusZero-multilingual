package uima.modules.ir.fulltext.indri.en

import modules.question.en.EnglishKnowledgeSourceSelector
import modules.util.ModulesConfig
import uima.modules.ir.fulltext.indri.MultiLingualRetrievalByBoW
import us.feliscat.m17n.English
import us.feliscat.ir.fulltext.indri.IndriResult
import us.feliscat.ir.fulltext.indri.en.EnglishTrecText
import us.feliscat.text.StringOption

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishRetrievalByBoW extends MultiLingualRetrievalByBoW with English {
  override protected val trecTextFormatData: Seq[String] = ModulesConfig.trecTextFormatDataInEnglish

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
