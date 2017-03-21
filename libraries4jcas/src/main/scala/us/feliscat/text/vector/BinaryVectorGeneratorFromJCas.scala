package us.feliscat.text.vector

import us.feliscat.types.{Sentence, TextAnnotation}
import us.feliscat.util.uima.StringListUtils._

/**
  * <pre>
  * Created on 2017/03/19.
  * </pre>
  *
  * @author K.Sakamoto
  */
object BinaryVectorGeneratorFromJCas extends VectorGeneratorFromJCas[BinaryVector] {

  override def getVectorFromSentences(sentences: Seq[Sentence]): BinaryVector = {
    BinaryVectorMerger.merge(
      for (sentence <- sentences) yield {
        getVectorFromAnnotation(sentence)
      }
    )
  }

  override def getVectorFromAnnotation(annotation: TextAnnotation): BinaryVector = {
    new BinaryVector(annotation.getContentWordList.toSeq)
  }
}
