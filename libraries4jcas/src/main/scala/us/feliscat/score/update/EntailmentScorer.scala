package us.feliscat.score.update

import us.feliscat.text.similarity.{AverageOverlapCalculator, OverlapCalculator}
import us.feliscat.text.vector.{BinaryVector, BinaryVectorGeneratorFromJCas, BinaryVectorMerger}
import us.feliscat.types.{Document, Score, Sentence}
import us.feliscat.util.uima.FSListUtils._
import us.feliscat.util.uima.StringListUtils._

import scala.collection.mutable.ListBuffer


/**
 * <pre>
 * Created on 3/11/15.
 * </pre>
 * @param instruction instruction
 * @param scoreIndex us.feliscat.score index
 * @author K.Sakamoto
 */
class EntailmentScorer(instruction: Document, scoreIndex: Int) extends Scorer {
  private val SCORER_NAME: String = "WordBasedEntailment"

  private lazy val convergenceCalculator: OverlapCalculator = {
    new OverlapCalculator(BinaryVectorGeneratorFromJCas.getVectorFromAnnotation(instruction))
  }

  private lazy val averageConvergenceCalculator: AverageOverlapCalculator = {
    new AverageOverlapCalculator({
      for (sentence <- instruction.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]) yield {
        BinaryVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
      }
    })
  }

  override def scoreBySentence(sentence: Sentence): Double = {
    val convergence: Double = averageConvergenceCalculator.
      calculate(new BinaryVector(sentence.getContentWordList.toSeq.distinct))
    val score: Score = sentence.getScoreList(scoreIndex)
    score.setScorer(SCORER_NAME)
    score.setScore(convergence)
    sentence.setScoreList(scoreIndex, score)
    convergence
  }

  override def scoreBySentences(document: Document): Double = {
    val score: Double = convergenceCalculator.calculate(
      BinaryVectorMerger.merge {
        val buffer = ListBuffer.empty[BinaryVector]
        for (sentence <- document.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]) {
          buffer += new BinaryVector(sentence.getContentWordList.toSeq.distinct)
        }
        buffer.result
      }
    )
    val scoreType: Score = document.getScoreList(scoreIndex)
    scoreType.setScorer(SCORER_NAME)
    scoreType.setScore(score)
    document.setScoreList(scoreIndex, scoreType)
    score
  }
}
