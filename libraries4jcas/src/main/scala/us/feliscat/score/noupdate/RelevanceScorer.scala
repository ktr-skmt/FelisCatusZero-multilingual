package us.feliscat.score.noupdate

import us.feliscat.text.similarity.{AverageSimilarityCalculator, SimilarityCalculator}
import us.feliscat.text.vector.{FrequencyVector, FrequencyVectorGeneratorFromJCas, FrequencyVectorMerger}
import us.feliscat.types.Sentence

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2016/12/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
class RelevanceScorer(summarySentences: Seq[Sentence]) extends Scorer {
  private lazy val similarityCalculator: SimilarityCalculator = {
    new SimilarityCalculator(FrequencyVectorGeneratorFromJCas.getVectorFromSentences(summarySentences))
  }

  private lazy val averageSimilarityCalculator: AverageSimilarityCalculator = {
    new AverageSimilarityCalculator({
      for (sentence <- summarySentences) yield {
        FrequencyVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
      }
    })
  }

  override def scoreBySentence(sentence: Sentence): Double = {
    averageSimilarityCalculator.calculate(
      FrequencyVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
    )
  }

  override def scoreBySentences(sentences: Seq[Sentence]): Double = {
    similarityCalculator.calculate(
      FrequencyVectorMerger.merge {
        val buffer = ListBuffer.empty[FrequencyVector]
        for (sentence <- sentences) {
          buffer += FrequencyVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
        }
        buffer.result
      }
    )
  }
}
