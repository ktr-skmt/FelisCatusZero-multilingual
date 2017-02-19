package ir.fulltext.indri.ja

import converter.ja.{JapaneseNgramSegmentator, JapaneseUnigramSegmentator}
import ir.fulltext.indri.{IndriResult, MultiLingualRetrievalByKeyword}
import m17n.Japanese
import question.ja.JapaneseKnowledgeSourceSelector
import text.StringOption

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/01/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseRetrievalByKeyword extends MultiLingualRetrievalByKeyword with Japanese {
  override protected def selectKnowledgeSource(isKeywordQuery: Boolean): Seq[String] = {
    JapaneseKnowledgeSourceSelector.select(isKeywordQuery)
  }

  override protected def segment(text: StringOption): StringOption = {
    JapaneseUnigramSegmentator.segmentator.asInstanceOf[JapaneseNgramSegmentator].segmentateWithCharacter(text)
  }

  override protected def toIndriResultMap(lines: Iterator[String],
                                          keywordOriginalTextOpt: StringOption,
                                          expansionOnlyList: Seq[String],
                                          indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    JapaneseTrecText.toIndriResultMap(lines, keywordOriginalTextOpt, expansionOnlyList, indriResultMap)
  }
}
