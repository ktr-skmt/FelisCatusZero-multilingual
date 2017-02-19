package uima.ae.en

import geography.en.EnglishGeographyExtractorForQuestion
import jeqa.types.{Query, Question, Sentence}
import org.apache.uima.jcas.JCas
import question.en.{EnglishQueryGenerator, EnglishQuestionFocusAnalyzer}
import time.TimeTmp
import time.en.EnglishTimeExtractorForQuestion
import uima.ae.MultiLingualQuestionAnalyzer

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishQuestionAnalyzer extends MultiLingualQuestionAnalyzer with EnglishDocumentAnnotator {
  override protected def extractTime(sentenceList: Seq[Sentence]): TimeTmp = {
    EnglishTimeExtractorForQuestion.extract(sentenceList)
  }

  override protected def extractGeography(sentenceList: Seq[Sentence]): (Seq[String], Seq[String]) = {
    EnglishGeographyExtractorForQuestion.extract(sentenceList)
  }

  override protected def analyzeQuestionFocus(sentenceSet: Seq[Sentence]): Seq[String] = {
    EnglishQuestionFocusAnalyzer.analyze(sentenceSet)
  }

  override protected def generateQuery(aJCas: JCas, question: Question): Seq[Query] = {
    EnglishQueryGenerator.generate(aJCas, question)
  }
}