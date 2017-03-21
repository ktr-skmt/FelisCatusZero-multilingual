package uima.cc.qalab

import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQALabSummarizationSubtaskCasConsumer
  extends MultiLingualQALabSubmission {
  override protected val mSubtaskOpt = StringOption("Summarization Subtask")

  /*
  <?xml version="1.0" encoding="UTF-8"?>
<answer_sheet ver="0.1" src="(PREASE WRITE A EXTRACTION FILE NAME)">
  <answer_section id="D792W10-1" label="D792W10_[1]">
    <question_id>q01</question_id>
    <answer_style>description_unlimited</answer_style>
    <answer_type>us.feliscat.sentence</answer_type>
    <knowledge_type>RT</knowledge_type>
    <instruction><p>Write an essay explaining, in 225 English words or less, how developments in the means of transportation and communication prompted the colonization of Asia and Africa and heightened local nationalism. Use all ninekeywordsshown below at least once.</p></instruction>
    <reference_set>
      <reference format="data" id="d01" is_directly_referred="0">We are currently living in an era of information revolution, and the pace of globalization is accelerating. Not only people and goods are flowing across oceans and borders with increasing frequency; information is being transmitted across the world almost in real us.feliscat.time. Underlying these developments is the rapid progress that has been made in transportation and communication technologies. Looking back over human history, we can find numerous instances where new developments in the means of transportation and communication have played important roles. In particular, from the mid-19th century through the early 20th century, such technological advances as wired and wireless telegraphy, the telephone, the camera, and cinematography came into practical use, resulting in the revolution that was audio-visual media. Furthermore, these technological innovations are noteworthy for the parts they played in Western nations' invasions of Asia and Africa. For example, the Reuters news agency gathered information from around the world to help develop the international presence of the British Empire. But, on the other hand, global information sharing and accelerated migration facilitated by the development of transportation were also stimulating factors in the growth of local nationalism.</reference>
      <reference format="data" id="d02" is_directly_referred="1">Suez Canal, steamship, Baghdad Railway, Morse code, Marconi, the Boxers, Russo-Japanese War, Persian Constitutional Revolution, Gandhi</reference>
    </reference_set>
    <keyword_set>
      <keyword>Suez Canal</keyword>
      <keyword>steamship</keyword>
      <keyword>Baghdad Railway</keyword>
      <keyword>Morse code</keyword>
      <keyword>Marconi</keyword>
      <keyword>the Boxers</keyword>
      <keyword>Russo-Japanese War</keyword>
      <keyword>Persian Constitutional Revolution</keyword>
      <keyword>Gandhi</keyword>
    </keyword_set>
    <answer_set type="singleton" number="1" >
      <us.feliscat.answer match_type="broad" order="-1" choices="" format_string="" length_limit="no more than 255 English words">
        <expression_set>
          <expression source_id="(passage_id1),(passage_id2),(passage_id3)">(PREASE WRITE YORE ANSWER)</expression>
                                </expression_set>
      </us.feliscat.answer>
    </answer_set>
  </answer_section>
    :
    :
  <answer_section id="M792W10-6" label="M792W10_[2]_Question (3)_(b)">
    <question_id>q09</question_id>
    <answer_style>description_limited</answer_style>
    <answer_type>us.feliscat.sentence</answer_type>
    <knowledge_type>KS</knowledge_type>
    <grand_question_set>
      <grand_question id="q02">Historically, nations called 'empires' mostly governed large areas that included multiple ethnic groups, races, and religions. In relation to the rise and fall of such empires, their differences and similarities, and their domestic and international relationships, us.feliscat.answer the three questions below. Write your responses in us.feliscat.answer section (B). Begin a new line for every uima.modules.qa.question you us.feliscat.answer and state the corresponding number (1) to (3) at the beginning.</grand_question>
      <grand_question id="q07">Answer the questions below in relation to the underlined sections (a)and(b), and state the corresponding letter (a) or (b) at the beginning.</grand_question>
    </grand_question_set>
    <instruction><p>Describe the characteristics of U.S. policy toward China after the Spanish-American War, in no more than 45 English words.</p></instruction>
    <reference_set>
    </reference_set>
    <answer_set type="singleton" number="1" >
      <us.feliscat.answer match_type="broad" order="-1" choices="" format_string="" length_limit="no more than 45 English words">
        <expression_set>
          <expression source_id="(passage_id1),(passage_id2),(passage_id3)">(PREASE WRITE YORE ANSWER)</expression>
        </expression_set>
      </us.feliscat.answer>
    </answer_set>
  </answer_section>
</answer_sheet>
   */
}
