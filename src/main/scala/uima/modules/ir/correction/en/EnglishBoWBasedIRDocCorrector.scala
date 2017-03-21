package uima.modules.ir.correction.en

import modules.time.en.EnglishTimeExtractorFromPreviousParagraphInTextbook
import us.feliscat.text.StringOption
import uima.modules.ir.correction.MultiLingualBoWBasedIRDocCorrector
import us.feliscat.text.normalizer.en.EnglishSentenceNormalizer
import us.feliscat.time.TimeTmp
import uima.modules.common.en.EnglishDocumentAnnotator
import us.feliscat.ir.fulltext.indri.en.EnglishTrecText

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishBoWBasedIRDocCorrector extends MultiLingualBoWBasedIRDocCorrector with EnglishDocumentAnnotator {
  override protected def extractTimeFromPreviousParagraph(docnoOpt: StringOption): TimeTmp = {
    EnglishTimeExtractorFromPreviousParagraphInTextbook.extractUnionTime(docnoOpt)
  }

  override protected def normalizeSentence(str: StringOption): StringOption = {
    EnglishSentenceNormalizer.normalize(str)
  }

  override protected def correct(text: String,
                                 keywordOriginalTextOpt: StringOption,
                                 expansionOnlyList: Seq[String]): String = {
    EnglishTrecText.correct(text, keywordOriginalTextOpt, expansionOnlyList)
  }
}
