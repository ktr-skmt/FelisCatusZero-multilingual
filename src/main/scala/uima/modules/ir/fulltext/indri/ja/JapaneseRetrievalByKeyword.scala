package uima.modules.ir.fulltext.indri.ja

import modules.question.ja.JapaneseKnowledgeSourceSelector
import uima.modules.ir.fulltext.indri.MultiLingualRetrievalByKeyword
import us.feliscat.converter.ja.{JapaneseNgramSegmentator, JapaneseUnigramSegmentator}
import us.feliscat.m17n.Japanese
import us.feliscat.ir.fulltext.indri.IndriResult
import us.feliscat.ir.fulltext.indri.ja.JapaneseTrecText
import us.feliscat.text.StringOption

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
