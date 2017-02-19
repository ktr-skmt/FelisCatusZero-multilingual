package text.correction.en

import ir.fulltext.indri.en.EnglishTrecText
import text.StringOption
import text.correction.MultiLingualBoWBasedIRDocCorrector
import text.normalizer.en.EnglishSentenceNormalizer
import time.TimeTmp
import time.en.EnglishTimeExtractorFromPreviousParagraphInTextbook
import uima.ae.en.EnglishDocumentAnnotator

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
