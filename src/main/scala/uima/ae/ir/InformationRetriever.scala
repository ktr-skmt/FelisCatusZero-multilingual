package uima.ae.ir

import org.apache.uima.UimaContext
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException
import uima.ae.ir.en.EnglishInformationRetriever
import uima.ae.ir.ja.JapaneseInformationRetriever
import uima.cpe.IntermediatePoint


/**
 * <p>質問解析結果により適切な知識源から解答に必要な記述を検索するプログラム</p>
 * @author K.Sakamoto
 *         Created on 15/10/30
 */
class InformationRetriever extends JCasAnnotator_ImplBase {
  @throws[ResourceInitializationException]
  override def initialize(aContext: UimaContext): Unit = {
    println(s">> ${IntermediatePoint.InformationRetriever.name} Initializing")
    super.initialize(aContext)
  }

  @throws[AnalysisEngineProcessException]
  override def process(aJCas: JCas): Unit = {
    println(s">> ${IntermediatePoint.InformationRetriever.name} Processing")

    JapaneseInformationRetriever.process(aJCas)
    EnglishInformationRetriever.process(aJCas)
  }
}