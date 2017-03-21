package us.feliscat.text.vector

import us.feliscat.sentence.ja.JapaneseSentenceSplitter
import us.feliscat.text.StringOption
import us.feliscat.text.analyzer.Tokenizer

/**
  * @author K.Sakamoto
  *         Created on 2016/05/22
  */
object BinaryVectorGenerator extends VectorGenerator[BinaryVector] {
  override def getVectorFromText(text: String): BinaryVector = {
    BinaryVectorMerger.merge(
      for (sentence <- JapaneseSentenceSplitter.split(StringOption(text))) yield {
        getVectorFromSentence(sentence.text)
      }
    )
  }

  override def getVectorFromSentence(sentence: String): BinaryVector = {
    new BinaryVector(Tokenizer.tokenize(StringOption(sentence)))
  }
}
