package uima.ae.ja

import geography.ja.JapaneseGeographyExtractorForQuestion
import jeqa.types.{Query, Question, Sentence}
import org.apache.uima.jcas.JCas
import question.ja.{JapaneseQueryGenerator, JapaneseQuestionFocusAnalyzer}
import time.TimeTmp
import time.ja.JapaneseTimeExtractorForQuestion
import uima.ae.MultiLingualQuestionAnalyzer

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseQuestionAnalyzer extends MultiLingualQuestionAnalyzer with JapaneseDocumentAnnotator {
  override protected def extractTime(sentenceList: Seq[Sentence]): TimeTmp = {
    JapaneseTimeExtractorForQuestion.extract(sentenceList)
  }

  override protected def extractGeography(sentenceList: Seq[Sentence]): (Seq[String], Seq[String]) = {
    JapaneseGeographyExtractorForQuestion.extract(sentenceList)
  }

  override protected def analyzeQuestionFocus(sentenceSet: Seq[Sentence]): Seq[String] = {
    JapaneseQuestionFocusAnalyzer.analyze(sentenceSet)
  }

  override protected def generateQuery(aJCas: JCas, question: Question): Seq[Query] = {
    JapaneseQueryGenerator.generate(aJCas, question)
  }
}