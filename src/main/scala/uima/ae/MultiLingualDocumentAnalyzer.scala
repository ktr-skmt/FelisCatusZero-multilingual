package uima.ae

import jeqa.types.Sentence
import m17n.MultiLingual
import org.apache.uima.jcas.JCas
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualDocumentAnalyzer extends MultiLingual {
  protected def extractContentWords(sentence: StringOption): Seq[String]
  protected def analyze(aJCas: JCas, sentence: Sentence, normalizedSentence: StringOption): Unit
}
