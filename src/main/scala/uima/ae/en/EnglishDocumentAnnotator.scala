package uima.ae.en

import jeqa.types.{TextAnnotation => UTextAnnotation}
import text.StringOption
import text.analyzer.CoreNLP4English
import time.TimeTmp
import time.en.EnglishTimeExtractorForWorldHistory
import uima.ae.MultiLingualDocumentAnnotator

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
