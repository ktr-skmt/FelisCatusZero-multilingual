package uima.cc

import text.StringOption

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQALabEvaluationMethodSubtaskCasConsumer
  extends MultiLingualQALabSubmission {
  override protected val mSubtaskOpt: StringOption = StringOption("Evaluation Method Subtask")

  /*
  <?xml version="1.0" encoding="UTF-8"?>
    <TOPIC ID="D792W10-1">
      <ANSWER_SET>
        <ANSWER FILE_NAME="(PREASE WRITE A E2E OR SUMMARIZATION RESULT FILE NAME)" RANK="1" SCORE="26">(ANSWER)</ANSWER>
        <ANSWER FILE_NAME="(PREASE WRITE A E2E OR SUMMARIZATION RESULT FILE NAME)" RANK="2" SCORE="24">(ANSWER)</ANSWER>
      </ANSWER_SET>
    </TOPIC>
*/
}
