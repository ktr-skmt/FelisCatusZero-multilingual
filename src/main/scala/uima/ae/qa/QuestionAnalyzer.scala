package uima.ae.qa

import org.apache.uima.UimaContext
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException
import uima.ae.qa.en.EnglishQuestionAnalyzer
import uima.ae.qa.ja.JapaneseQuestionAnalyzer
import uima.cpe.IntermediatePoint

/**
  * <p>
  *   ExamをQuestionに分解して、それぞれのQuestion Format Typeを推定する。
  *   もし、Question Format Typeが論述なら
  *   <ul>
  *     <li>検索する知識源の種類</li>
  *     <li>必要な記述を検索するためのクエリ</li>
  *     <li>時間・地域など検索結果を絞り込む条件</li>
  *     <li>解候補の適切性を計測するために使用する情報</li>
  *   </ul>
  *   を抽出・生成・推定する。
  * </p>
  * @author K.Sakamoto
  *         Created on 15/10/30
  */
class QuestionAnalyzer extends JCasAnnotator_ImplBase {
  @throws[ResourceInitializationException]
  override def initialize(aContext: UimaContext): Unit = {
    println(s">> ${IntermediatePoint.QuestionAnalyzer.name} Initializing")
    super.initialize(aContext)
  }

  @throws[AnalysisEngineProcessException]
  override def process(aJCas: JCas): Unit = {
    println(s">> ${IntermediatePoint.QuestionAnalyzer.name} Processing")

    JapaneseQuestionAnalyzer.process(aJCas)
    EnglishQuestionAnalyzer.process(aJCas)
  }
}
