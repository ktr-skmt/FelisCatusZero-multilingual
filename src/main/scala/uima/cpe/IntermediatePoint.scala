package uima.cpe

import us.feliscat.text.{StringNone, StringOption, StringSome}

/**
  * <pre>
  * Created on 2016/12/18.
  * </pre>
  *
  * @author K.Sakamoto
  */
object IntermediatePoint {
  case object QuestionReader       extends IntermediatePoint(0) {
    override val name: String = "QuestionReader"
    override val code: String = "qr"
  }
  case object QuestionAnalyzer     extends IntermediatePoint(1) {
    override val name: String = "QuestionAnalyzer"
    override val code: String = "qa"
    override val descriptor = StringOption("questionAnalyzerAAEDescriptor")
    override val primitiveDescriptor = StringOption("questionAnalyzerAEDescriptor")
  }
  case object InformationRetriever extends IntermediatePoint(2) {
    override val name: String = "InformationRetriever"
    override val code: String = "ir"
    override val descriptor = StringOption("informationRetrieverAAEDescriptor")
    override val primitiveDescriptor = StringOption("informationRetrieverAEDescriptor")
  }
  case object AnswerGenerator      extends IntermediatePoint(3) {
    override val name: String = "AnswerGenerator"
    override val code: String = "ag"
    override val descriptor = StringOption("answerGeneratorAAEDescriptor")
    override val primitiveDescriptor = StringOption("answerGeneratorAEDescriptor")
  }
  case object AnswerWriter         extends IntermediatePoint(4) {
    override val name: String = "AnswerWriter"
    override val code: String = "aw"
    override val descriptor = StringOption("answerWriterCCDescriptor")
  }
  case object AnswerEvaluator      extends IntermediatePoint(5) {
    override val name: String = "AnswerEvaluator"
    override val code: String = "ae"
    override val descriptor = StringOption("answerEvaluatorCCDescriptor")
  }

  val pipeline = Seq(
    QuestionReader,
    QuestionAnalyzer,
    InformationRetriever,
    AnswerGenerator,
    AnswerWriter,
    AnswerEvaluator
  )

  def get(code: StringOption, default: IntermediatePoint): IntermediatePoint = {
    code match {
      case StringSome(ip) =>
        pipeline foreach {
          case component if component.equals(ip) =>
            return component
          case _ =>
            // Do nothing
        }
        default
      case StringNone =>
        default
    }
  }
}

sealed abstract class IntermediatePoint(val id: Int) {
  val name: String
  val code: String
  val descriptor:          StringOption = StringOption.empty
  val primitiveDescriptor: StringOption = StringOption.empty
  def equals(nameOrCode: String): Boolean = {
    if (Option(nameOrCode).isEmpty) {
      return false
    }
    name.equalsIgnoreCase(nameOrCode) || code.equalsIgnoreCase(nameOrCode)
  }
}
