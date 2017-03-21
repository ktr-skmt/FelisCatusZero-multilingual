package uima.modules.ir.correction.ja

import modules.time.ja.JapaneseTimeExtractorFromPreviousParagraphInTextbook
import us.feliscat.text.StringOption
import uima.modules.ir.correction.MultiLingualKeywordBasedIRDocCorrector
import us.feliscat.text.normalizer.ja.JapaneseSentenceNormalizer
import us.feliscat.time.TimeTmp
import uima.modules.common.ja.JapaneseDocumentAnnotator
import us.feliscat.ir.fulltext.indri.ja.JapaneseTrecText

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseKeywordBasedIRDocCorrector extends MultiLingualKeywordBasedIRDocCorrector with JapaneseDocumentAnnotator {
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
