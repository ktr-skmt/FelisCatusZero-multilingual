package uima.ae.qa.ja

import org.apache.uima.jcas.JCas
import uima.ae.qa.MultiLingualQuestionAnalyzer
import uima.modules.common.ja.JapaneseDocumentAnnotator
import uima.modules.qa.ja.{JapaneseQueryGenerator, JapaneseQuestionFocusAnalyzer}
import us.feliscat.geography.ja.JapaneseGeographyExtractorForQuestion
import us.feliscat.time.TimeTmp
import us.feliscat.time.ja.JapaneseTimeExtractorForQuestion
import us.feliscat.types.{Query, Question, Sentence}

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