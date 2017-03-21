package uima.modules.ir.fulltext.indri.en

import modules.question.en.EnglishKnowledgeSourceSelector
import uima.modules.ir.fulltext.indri.MultiLingualRetrievalByKeyword
import us.feliscat.converter.en.{EnglishNgramSegmentator, EnglishUnigramSegmentator}
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
object EnglishRetrievalByKeyword extends MultiLingualRetrievalByKeyword with English {
  override protected def selectKnowledgeSource(isKeywordQuery: Boolean): Seq[String] = {
    EnglishKnowledgeSourceSelector.select(isKeywordQuery)
  }

  override protected def segment(text: StringOption): StringOption = {
    EnglishUnigramSegmentator.segmentator.asInstanceOf[EnglishNgramSegmentator].segmentateWithToken(text.getOrElse("").split(' '))
  }

  override protected def toIndriResultMap(lines: Iterator[String],
                                 keywordOriginalTextOpt: StringOption,
                                 expansionOnlyList: Seq[String],
                                 indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult] = {
    EnglishTrecText.toIndriResultMap(lines, keywordOriginalTextOpt, expansionOnlyList, indriResultMap)
  }
}
