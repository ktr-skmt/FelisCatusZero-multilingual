package uima.cc.qalab

import org.apache.uima.cas.CAS
import org.apache.uima.collection.CasConsumer_ImplBase
import org.apache.uima.resource.ResourceProcessException
import uima.cc.qalab.en.{EnglishQALabExtractionSubtaskDocumentCasConsumer, EnglishQALabExtractionSubtaskSentenceCasConsumer}
import uima.cc.qalab.ja.{JapaneseQALabExtractionSubtaskDocumentCasConsumer, JapaneseQALabExtractionSubtaskSentenceCasConsumer}

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

    JapaneseQALabExtractionSubtaskSentenceCasConsumer.process(aCAS)
    EnglishQALabExtractionSubtaskSentenceCasConsumer.process(aCAS)

    JapaneseQALabExtractionSubtaskDocumentCasConsumer.process(aCAS)
    EnglishQALabExtractionSubtaskDocumentCasConsumer.process(aCAS)
  }
}
