package uima.cpe

import org.kohsuke.args4j.Option

import scala.beans.BeanProperty

/**
  * <pre>
  * Created on 2016/12/18.
  * </pre>
  *
  * @author K.Sakamoto
  */
class CPERunnerOption() {
  @Option(name = "-doJCasGen", aliases = Array[String]("--generateJCas"), usage = "do JCasGen as pre-processing", required = false)
  @BeanProperty
  var doJCasGen: Boolean = false

  @Option(name = "-doCharacterLevelIndriIndexInJapanese", aliases = Array[String]("--generateCharacterLevelIndriIndexInJapanese"), usage = "do IndriIndex with character as pre-processing", required = false)
  @BeanProperty
  var doCharacterLevelIndriIndexInJapanese: Boolean = false

  @Option(name = "-doContentWordLevelIndriIndexInJapanese", aliases = Array[String]("--generateContentWordLevelIndriIndexInJapanese"), usage = "do IndriIndex with content word as pre-processing", required = false)
  @BeanProperty
  var doContentWordLevelIndriIndexInJapanese: Boolean = false

  @Option(name = "-doTokenLevelIndriIndexInEnglish", aliases = Array[String]("--generateTokenLevelIndriIndexInEnglish"), usage = "do IndriIndex with English token as pre-processing", required = false)
  @BeanProperty
  var doTokenLevelIndriIndexInEnglish: Boolean = false

  @Option(name = "-doContentWordLevelIndriIndexInEnglish", aliases = Array[String]("--generateContentWordLevelIndriIndexInEnglish"), usage = "do IndriIndex with English content word as pre-processing", required = false)
  @BeanProperty
  var doContentWordLevelIndriIndexInEnglish: Boolean = false

  @Option(name = "-doFastText", aliases = Array[String]("--generateFastTextModel"), usage = "do fastText as pre-processing", required = false)
  @BeanProperty
  var doFastText: Boolean = false

  @Option(name = "-from", aliases = Array[String]("--startPoint"), usage = "start point: cr (EssayQuestionReader) = default, qa (QuestionAnalyzer), ir (InformationRetriever), eg (EssayGenerator), w (EssayWriter), e (EssayEvaluator)", required = false)
  @BeanProperty
  var startPoint: String = "cr"

  @Option(name = "-to", aliases = Array[String]("--endPoint"), usage = "end point: cr (EssayQuestionReader), qa (QuestionAnalyzer), ir (InformationRetriever), eg (EssayGenerator), w (EssayWriter), e (EssayEvaluator) = default", required = false)
  @BeanProperty
  var endPoint: String = "e"

  @Option(name = "-unSave", aliases = Array[String]("--unSaveIntermediateState"), usage = "unsave intermediate state. If you want to unsave all state, use '-unSave=all' or '-unSave=qa,ir,eg'. If you want to unsave the qa state, use '-unSave=qa'. If you want to unsave the qa and eg states, use '-unSave=qa,eg'.", required = false)
  @BeanProperty
  var unSave: String = ""

  @Option(name = "-qalabExtraction", aliases = Array[String]("--wantToOutputForQALabExtractionSubtask"), usage = "want to output submission for QA Lab Extraction Subtask", required = false)
  @BeanProperty
  var wantToOutputForQALabExtractionSubtask: Boolean = false

  @Option(name = "-qalabSummarization", aliases = Array[String]("--wantToOutputForQALabSummarizationSubtask"), usage = "want to output submission for QA Lab Summarization Subtask", required = false)
  @BeanProperty
  var wantToOutputForQALabSummarizationSubtask: Boolean = false

  @Option(name = "-qalabEvaluationMethod", aliases = Array[String]("--wantToOutputForQALabEvaluationMethodSubtask"), usage = "want to output submission for QA Lab Evaluation Method Subtask", required = false)
  @BeanProperty
  var wantToOutputForQALabEvaluationMethodSubtask: Boolean = false
}
