package uima.ae.ag

import java.util.Locale

import org.apache.uima.UimaContext
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException
import uima.ae.ag.en.EnglishAnswerGenerator
import uima.ae.ag.ja.JapaneseAnswerGenerator
import uima.cpe.IntermediatePoint
import us.feliscat.util.uima.JCasID

/**
 * <p>日本語の論述問題を解くプログラム</p>
 * Coverage + MMR
 * @author K.Sakamoto
 *         Created on 15/10/30
 */
class AnswerGenerator extends JCasAnnotator_ImplBase {
  @throws[ResourceInitializationException]
  override def initialize(aContext: UimaContext): Unit = {
    println(s">> ${IntermediatePoint.AnswerGenerator.name} Initializing")

    super.initialize(aContext)
  }

  @throws[AnalysisEngineProcessException]
  override def process(aJCas: JCas): Unit = {
    println(s">> ${IntermediatePoint.AnswerGenerator.name} Processing")

    JapaneseAnswerGenerator.process(aJCas)(JCasID(Locale.JAPANESE.getLanguage))
    EnglishAnswerGenerator.process(aJCas)(JCasID(Locale.ENGLISH.getLanguage))
  }
}
