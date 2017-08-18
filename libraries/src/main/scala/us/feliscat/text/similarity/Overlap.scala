package us.feliscat.text.similarity

import us.feliscat.text.analyzer.Tokenizer.Tokens
import us.feliscat.text.vector.BinaryVector
import us.feliscat.util.LibrariesConfig

import scala.collection.mutable.ArrayBuffer

/**
  * @author K.Sakamoto
  *         Created on 2016/05/23
  */
object Overlap extends Enumeration {
  val None,
  F,//similarity
  F1,//similarity
  Precision,//divergence
  Recall,//convergence
  Rus05,//convergence
  WordEmbedding
  = Value

  def calculate(v1: BinaryVector, v2: BinaryVector, convergence: Overlap.Value): Double = {
    convergence match {
      case F =>
        calculateF(v1, v2, LibrariesConfig.fScoreBeta)
      case F1 =>
        calculateF1(v1, v2)
      case Precision =>
        calculatePrecision(v1, v2)
      case Recall | Rus05 =>
        calculateRecall(v1, v2)
      case WordEmbedding =>
        calculateWordEmbedding(v1, v2)
      case _ =>
        0D
    }
  }

  def calculatePrecision(v1: BinaryVector, v2: BinaryVector): Double = {
    calculateRecall(v2, v1)
  }

  def calculatePrecisionWithLCS(tokens1: Tokens, tokens2: Tokens): Double = {
    calculateRecallWithLCS(tokens2, tokens1)
  }

  def calculateRecall(v1: BinaryVector, v2: BinaryVector): Double = {
    calculateRecallWithLCS(v1.vector, v2.vector)
  }

  def calculateRecallWithLCS(tokens1: Tokens, tokens2: Tokens): Double = {
    Divider.divide(tokens1.intersect(tokens2).size.toDouble, tokens1.size)
  }

  def calculateF1(v1: BinaryVector, v2: BinaryVector): Double = {
    calculateF(v1, v2, 1D)
  }

  def calculateF1WithLCS(tokens1: Tokens, tokens2: Tokens): Double = {
    calculateFWithLCS(tokens1, tokens2, 1D)
  }

  def calculateF(v1: BinaryVector, v2: BinaryVector, beta: Double): Double = {
    calculateF(
      calculatePrecision(v1, v2),
      calculateRecall(v1, v2),
      beta)
  }

  def calculateFWithLCS(tokens1: Tokens, tokens2: Tokens, beta: Double): Double = {
    calculateF(
      calculatePrecisionWithLCS(tokens1, tokens2),
      calculateRecallWithLCS(tokens1, tokens2),
      beta)
  }

  def calculateF(precision: Double, recall: Double, beta: Double): Double = {
    val betaPow2: Double = beta * beta
    Divider.divide((1 + betaPow2) * precision * recall, betaPow2 * precision + recall)
  }

  def calculateWordEmbedding(v1: BinaryVector, v2: BinaryVector): Double = {
    def score(keywords: Seq[String], passage: Seq[String]):Double = {
      var finalScore: Double = 0
      passage foreach {
        word: String => {
          val maxPassageWordScore = ArrayBuffer.empty[Double]
          keywords foreach {
            keyword: String => {
              //TODO
              val similarity: Double = 0D//word2vec.calcSimilarity(keyword, word)
              maxPassageWordScore += similarity
            }
          }
          if (maxPassageWordScore.nonEmpty) {
            finalScore += maxPassageWordScore.max
          }
          maxPassageWordScore.clear
        }
      }
      val size: Int = passage.size
      if (0 < size) {
        finalScore / size
      } else {
        0D
      }
    }

    score(v1.vector, v2.vector) match {
      case s if s.isNaN =>
        0D
      case s =>
        s
    }
  }
}
