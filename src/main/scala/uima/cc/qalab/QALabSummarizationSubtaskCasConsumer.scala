package uima.cc.qalab

import org.apache.uima.cas.CAS
import org.apache.uima.collection.CasConsumer_ImplBase
import org.apache.uima.resource.ResourceProcessException
import uima.cc.qalab.en.EnglishQALabSummarizationSubtaskCasConsumer
import uima.cc.qalab.ja.JapaneseQALabSummarizationSubtaskCasConsumer

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
class QALabSummarizationSubtaskCasConsumer extends CasConsumer_ImplBase {
  override def initialize(): Unit = {
    println(">> QA Lab Summarization Subtask Cas Consumer Initializing")
    super.initialize()
  }

  @throws[ResourceProcessException]
  override def processCas(aCAS: CAS): Unit = {
    println(">> QA Lab Summarization Subtask Cas Consumer Processing")

    JapaneseQALabSummarizationSubtaskCasConsumer.process(aCAS)
    EnglishQALabSummarizationSubtaskCasConsumer.process(aCAS)
  }
}