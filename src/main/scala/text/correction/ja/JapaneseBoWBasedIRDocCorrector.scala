package text.correction.ja

import ir.fulltext.indri.ja.JapaneseTrecText
import text.StringOption
import text.correction.MultiLingualBoWBasedIRDocCorrector
import text.normalizer.ja.JapaneseSentenceNormalizer
import time.TimeTmp
import time.ja.JapaneseTimeExtractorFromPreviousParagraphInTextbook
import uima.ae.ja.JapaneseDocumentAnnotator

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseBoWBasedIRDocCorrector extends MultiLingualBoWBasedIRDocCorrector with JapaneseDocumentAnnotator {
  override protected def extractTimeFromPreviousParagraph(docnoOpt: StringOption): TimeTmp = {
    JapaneseTimeExtractorFromPreviousParagraphInTextbook.extractUnionTime(docnoOpt)
  }

  override protected def normalizeSentence(str: StringOption): StringOption = {
    JapaneseSentenceNormalizer.normalize(str)
  }

  override protected def correct(text: String,
                                 keywordOriginalTextOpt: StringOption,
                                 expansionOnlyList: Seq[String]): String = {
    JapaneseTrecText.correct(text, keywordOriginalTextOpt, expansionOnlyList)
  }
}
