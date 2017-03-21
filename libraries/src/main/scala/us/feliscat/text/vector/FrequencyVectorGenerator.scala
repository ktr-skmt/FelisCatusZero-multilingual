package us.feliscat.text.vector

import us.feliscat.sentence.ja.JapaneseSentenceSplitter
import us.feliscat.text.StringOption
import us.feliscat.text.analyzer.Tokenizer

import scala.collection.mutable

/**
  * @author K.Sakamoto
  *         Created on 2016/05/22
  */
object FrequencyVectorGenerator extends VectorGenerator[FrequencyVector] {
  override def getVectorFromText(text: String): FrequencyVector = {
    getVector(
      //Seq[Seq[(String, Int)]]からList[Set[(String, Int)]]を経由してSeq[(String, Int)]に変換した。
      {
        for (sentence <- JapaneseSentenceSplitter.split(StringOption(text))) yield {
          getVectorFromSentence(sentence.text).vector.toSet
        }
      }.toList.flatten
    )
  }

  override def getVectorFromSentence(sentence: String): FrequencyVector = {
    getVector(
      Tokenizer.tokenize(StringOption(sentence)).zipAll(Nil, "", 1)
    )
  }

  def getVector(terms: Seq[(String, Int)]): FrequencyVector = {
    val vector = mutable.Map.empty[String, Int]

    def add(term: String, frequency: Int): Unit = {
      if (vector contains term) {
        vector(term) += frequency
      } else {
        vector(term) = frequency
      }
    }

    terms foreach {
      case (term, frequency) =>
        add(term, frequency)
    }

    val frequencyVector = new FrequencyVector(vector)

    VectorType.get match {
      case VectorType.Binary =>
        frequencyVector.
          toBinaryVector.
          toFrequencyVector
      case VectorType.Frequency |
           VectorType.None =>
        frequencyVector
    }
  }
}
