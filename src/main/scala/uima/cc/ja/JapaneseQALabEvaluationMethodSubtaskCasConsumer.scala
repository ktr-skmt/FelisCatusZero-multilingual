package uima.cc.ja

import m17n.Japanese
import org.apache.uima.jcas.JCas
import uima.cc.MultiLingualQALabEvaluationMethodSubtaskCasConsumer

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseQALabEvaluationMethodSubtaskCasConsumer
  extends MultiLingualQALabEvaluationMethodSubtaskCasConsumer with Japanese {
  override protected def process(aJCas: JCas): Unit = {

  }
}
