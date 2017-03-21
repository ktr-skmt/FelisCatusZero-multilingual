package uima.modules.common.en

import us.feliscat.m17n.English
import org.apache.uima.jcas.JCas
import uima.modules.common.MultiLingualDocumentAnalyzer
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.text.analyzer.CoreNLP4English
import us.feliscat.types.{CoreNLPAnalysis, Sentence}
import us.feliscat.util.uima.FeatureStructure
import us.feliscat.util.uima.SeqStringUtils._
import us.feliscat.util.uima.SeqUtils._

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
