package uima.modules.common.en

import uima.modules.common.MultiLingualDocumentAnnotator
import us.feliscat.types.{TextAnnotation => UTextAnnotation}
import us.feliscat.text.StringOption
import us.feliscat.text.analyzer.CoreNLP4English
import us.feliscat.time.TimeTmp
import us.feliscat.time.en.EnglishTimeExtractorForWorldHistory

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait EnglishDocumentAnnotator extends MultiLingualDocumentAnnotator with EnglishDocumentAnalyzer {
  override protected def ssplit(textOpt: StringOption): Seq[String] = {
    CoreNLP4English.ssplit(textOpt).map(_.trim)
  }

  override protected def extractTime(sentenceOpt: StringOption): TimeTmp = {
    EnglishTimeExtractorForWorldHistory.extractUnionTime(sentenceOpt)
  }

  override protected def correct(text: UTextAnnotation, keywordSet: Seq[String]): Unit = {
    //TODO:
  }
}
