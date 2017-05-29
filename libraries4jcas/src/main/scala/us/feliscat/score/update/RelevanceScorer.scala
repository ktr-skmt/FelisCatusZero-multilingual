package us.feliscat.score.update

import us.feliscat.text.similarity.{AverageSimilarityCalculator, SimilarityCalculator}
import us.feliscat.text.vector._
import us.feliscat.types.{Document, Score, Sentence}
import us.feliscat.util.uima.fsList.FSListUtils

import scala.collection.mutable.ListBuffer

/**
 * <pre>
 * Created on 3/11/15.
 * </pre>
 * @param instruction instruction
 * @param scoreIndex score index
 * @author K.Sakamoto
 */
class RelevanceScorer(instruction: Document, scoreIndex: Int) extends Scorer {
  private val SCORER_NAME: String = "WordBasedRelevanceScorer"

  private lazy val similarityCalculator: SimilarityCalculator = {
    new SimilarityCalculator(FrequencyVectorGeneratorFromJCas.getVectorFromAnnotation(instruction))
  }

  private lazy val averageSimilarityCalculator: AverageSimilarityCalculator = {
    new AverageSimilarityCalculator({
      for (sentence <- instruction.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]) yield {
        FrequencyVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
      }
    })
  }

  override def scoreBySentence(sentence: Sentence): Double = {
    val similarity: Double = averageSimilarityCalculator.calculate(
      FrequencyVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
    )
    val score: Score = sentence.getScoreList(scoreIndex)
    score.setScorer(SCORER_NAME)
    score.setScore(similarity)
    sentence.setScoreList(scoreIndex, score)
    similarity
  }

  override def scoreBySentences(document: Document): Double = {
    val score: Double = similarityCalculator.calculate(
      FrequencyVectorMerger.merge {
        val buffer = ListBuffer.empty[FrequencyVector]
        for (sentence <- document.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]) {
          buffer += FrequencyVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
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
