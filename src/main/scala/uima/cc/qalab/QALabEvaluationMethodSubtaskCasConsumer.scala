package uima.cc.qalab

import java.util.Locale

import org.apache.uima.cas.CAS
import org.apache.uima.collection.CasConsumer_ImplBase
import org.apache.uima.resource.ResourceProcessException
import uima.cc.qalab.en.EnglishQALabEvaluationMethodSubtaskCasConsumer
import uima.cc.qalab.ja.JapaneseQALabEvaluationMethodSubtaskCasConsumer
import us.feliscat.util.uima.JCasID

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

    JapaneseQALabEvaluationMethodSubtaskCasConsumer.process(aCAS)(JCasID(Locale.JAPANESE.getLanguage))
    EnglishQALabEvaluationMethodSubtaskCasConsumer.process(aCAS)(JCasID(Locale.ENGLISH.getLanguage))
  }
}