package us.feliscat.score.noupdate

import us.feliscat.text.similarity.{AverageOverlapCalculator, OverlapCalculator}
import us.feliscat.text.vector._
import us.feliscat.types.Sentence

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2016/12/09.
  * </pre>
  * @param summarySentences summary sentences
  * @author K.Sakamoto
  */
class EntailmentScorer(summarySentences: Seq[Sentence]) extends Scorer {
  private lazy val convergenceCalculator: OverlapCalculator = {
    new OverlapCalculator(BinaryVectorGeneratorFromJCas.getVectorFromSentences(summarySentences))
  }

  private lazy val averageConvergenceCalculator: AverageOverlapCalculator = {
    new AverageOverlapCalculator({
      for (sentence <- summarySentences) yield {
        BinaryVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
      }
    })
  }

  override def scoreBySentence(sentence: Sentence): Double = {
    averageConvergenceCalculator.calculate(
      BinaryVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
    )
  }

  override def scoreBySentences(sentences: Seq[Sentence]): Double = {
    convergenceCalculator.calculate(
      BinaryVectorMerger.merge {
        val buffer = ListBuffer.empty[BinaryVector]
        for (sentence <- sentences) {
          buffer += BinaryVectorGeneratorFromJCas.getVectorFromAnnotation(sentence)
        }
        buffer.result
      }
    )
  }
}
