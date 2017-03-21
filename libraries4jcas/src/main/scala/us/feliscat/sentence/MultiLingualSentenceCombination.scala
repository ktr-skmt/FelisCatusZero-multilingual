package us.feliscat.sentence

import us.feliscat.types.Sentence

import scala.beans.BeanProperty

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualSentenceCombination {
  @BeanProperty
  var sentences: List[Sentence] = List.empty[Sentence]

  @BeanProperty
  var number: Int = 0

  @BeanProperty
  var score: Double = 0D

  @BeanProperty
  var linkScore: Double = 0D

  @BeanProperty
  var mergedScores: Seq[Double] = Seq.empty[Double]

  def contains(sentence: Sentence): Boolean = {
    if (sentences.contains(sentence)) {
      return true
    }
    val text: String = sentence.getText
    sentences foreach {
      s: Sentence =>
        if (s.getText == text) {
          return true
        }
    }
    false
  }
}