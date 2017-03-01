package uima.ae.en

import jeqa.types.{CoreNLPAnalysis, Sentence}
import m17n.English
import org.apache.uima.jcas.JCas
import text.{StringNone, StringOption, StringSome}
import text.analyzer.CoreNLP4English
import uima.ae.MultiLingualDocumentAnalyzer
import util.uima.FeatureStructure
import util.uima.SeqStringUtils._
import util.uima.SeqUtils._

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait EnglishDocumentAnalyzer extends MultiLingualDocumentAnalyzer with English {
  override protected def extractContentWords(sentence: StringOption): Seq[String] = {
    CoreNLP4English.extractContentWords(sentence)
  }

  override protected def analyze(aJCas: JCas, sentence: Sentence, normalizedSentence: StringOption): Unit = {
    val contentWords: Seq[String] = extractContentWords(normalizedSentence)
    sentence.setContentWordList(contentWords.toStringList)
    val analysis = FeatureStructure.create[CoreNLPAnalysis]
    //analysis.setLemmaList()
    //analysis.setPartOfSpeechList()
    //analysis.setStemmedWordList()
    //analysis.setTokenList()
    CoreNLP4English.analysisResult(normalizedSentence) match {
      case StringSome(result) =>
        analysis.setAnalysisResult(result)
      case StringNone =>
        // Do nothing
    }
    sentence.setCoreNLPAnalysisList(Seq[CoreNLPAnalysis](analysis).toFSList)
  }
}
