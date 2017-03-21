package uima.cc.qalab.en

import us.feliscat.m17n.English
import org.apache.uima.jcas.JCas
import uima.cc.qalab.MultiLingualQALabEvaluationMethodSubtaskCasConsumer

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishQALabEvaluationMethodSubtaskCasConsumer
  extends MultiLingualQALabEvaluationMethodSubtaskCasConsumer with English {
  override protected def process(aJCas: JCas): Unit = {

  }
}
