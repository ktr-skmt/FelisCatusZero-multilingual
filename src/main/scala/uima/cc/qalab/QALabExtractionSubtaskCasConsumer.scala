package uima.cc.qalab

import java.util.Locale

import org.apache.uima.cas.CAS
import org.apache.uima.collection.CasConsumer_ImplBase
import org.apache.uima.resource.ResourceProcessException
import uima.cc.qalab.en.{EnglishQALabExtractionSubtaskDocumentCasConsumer, EnglishQALabExtractionSubtaskSentenceCasConsumer}
import uima.cc.qalab.ja.{JapaneseQALabExtractionSubtaskDocumentCasConsumer, JapaneseQALabExtractionSubtaskSentenceCasConsumer}
import us.feliscat.util.uima.JCasID

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
class QALabExtractionSubtaskCasConsumer extends CasConsumer_ImplBase {
  override def initialize(): Unit = {
    println(">> QA Lab Extraction Subtask Cas Consumer Initializing")
    super.initialize()
  }

  @throws[ResourceProcessException]
  override def processCas(aCAS: CAS): Unit = {
    println(">> QA Lab Extraction Subtask Cas Consumer Processing")

    JapaneseQALabExtractionSubtaskSentenceCasConsumer.process(aCAS)(JCasID(Locale.JAPANESE.getLanguage))
    EnglishQALabExtractionSubtaskSentenceCasConsumer.process(aCAS)(JCasID(Locale.ENGLISH.getLanguage))

    JapaneseQALabExtractionSubtaskDocumentCasConsumer.process(aCAS)(JCasID(Locale.JAPANESE.getLanguage))
    EnglishQALabExtractionSubtaskDocumentCasConsumer.process(aCAS)(JCasID(Locale.ENGLISH.getLanguage))
  }
}
