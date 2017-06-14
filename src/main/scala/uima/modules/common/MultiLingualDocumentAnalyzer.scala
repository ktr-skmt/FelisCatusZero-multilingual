package uima.modules.common

import org.apache.uima.jcas.JCas
import us.feliscat.m17n.MultiLingual
import us.feliscat.text.StringOption
import us.feliscat.types.Sentence
import us.feliscat.util.uima.JCasID

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualDocumentAnalyzer extends MultiLingual {
  protected def extractContentWords(sentence: StringOption): Seq[String]
  protected def analyze(aJCas: JCas, sentence: Sentence, normalizedSentence: StringOption)(implicit id: JCasID): Unit
}
