package uima.cc.qalab

import org.apache.uima.cas.CAS
import org.apache.uima.collection.CasConsumer_ImplBase
import org.apache.uima.resource.ResourceProcessException
import uima.cc.qalab.en.EnglishQALabEvaluationMethodSubtaskCasConsumer
import uima.cc.qalab.ja.JapaneseQALabEvaluationMethodSubtaskCasConsumer

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
class QALabEvaluationMethodSubtaskCasConsumer extends CasConsumer_ImplBase {
  override def initialize(): Unit = {
    println(">> QA Lab Evaluation Method Subtask Cas Consumer Initializing")
    super.initialize()
  }

  @throws[ResourceProcessException]
  override def processCas(aCAS: CAS): Unit = {
    println(">> QA Lab Evaluation Method Subtask Cas Consumer Processing")

    JapaneseQALabEvaluationMethodSubtaskCasConsumer.process(aCAS)
    EnglishQALabEvaluationMethodSubtaskCasConsumer.process(aCAS)
  }
}