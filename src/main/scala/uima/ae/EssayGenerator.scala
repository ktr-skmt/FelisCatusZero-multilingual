package uima.ae

import org.apache.uima.UimaContext
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException
import uima.ae.en.EnglishEssayGenerator
import uima.ae.ja.JapaneseEssayGenerator

/**
 * <p>日本語の論述問題を解くプログラム</p>
 * Coverage + MMR
 * @author K.Sakamoto
 *         Created on 15/10/30
 */
class EssayGenerator extends JCasAnnotator_ImplBase {
  @throws[ResourceInitializationException]
  override def initialize(aContext: UimaContext): Unit = {
    println(">> Essay Generator Initializing")

    super.initialize(aContext)
  }

  @throws[AnalysisEngineProcessException]
  override def process(aJCas: JCas): Unit = {
    println(">> Essay Generator Processing")

    JapaneseEssayGenerator.process(aJCas)
    EnglishEssayGenerator.process(aJCas)
  }
}
