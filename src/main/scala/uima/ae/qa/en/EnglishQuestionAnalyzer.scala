package uima.ae.qa.en

import org.apache.uima.jcas.JCas
import uima.ae.qa.MultiLingualQuestionAnalyzer
import uima.modules.common.en.EnglishDocumentAnnotator
import uima.modules.qa.en.{EnglishQueryGenerator, EnglishQuestionFocusAnalyzer}
import us.feliscat.geography.en.EnglishGeographyExtractorForQuestion
import us.feliscat.time.TimeTmp
import us.feliscat.time.en.EnglishTimeExtractorForQuestion
import us.feliscat.types.{Query, Question, Sentence}

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