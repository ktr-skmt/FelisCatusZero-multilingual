package uima.cc.ja

import m17n.Japanese
import org.apache.uima.jcas.JCas
import uima.cc.MultiLingualQALabSummarizationSubtaskCasConsumer

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseQALabSummarizationSubtaskCasConsumer
  extends MultiLingualQALabSummarizationSubtaskCasConsumer with Japanese {
  override protected def process(aJCas: JCas): Unit = {

  }
}
