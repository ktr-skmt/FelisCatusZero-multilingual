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
object FrequencyVectorGeneratorFromJCas extends VectorGeneratorFromJCas[FrequencyVector] {
  override def getVectorFromSentences(sentences: Seq[Sentence]): FrequencyVector = {
    FrequencyVectorGenerator.getVector(
      {
        for (sentence <- sentences) yield {
          getVectorFromAnnotation(sentence).vector.toSet
        }
      }.toList.flatten
    )
  }

  override def getVectorFromAnnotation(annotation: TextAnnotation): FrequencyVector = {
    FrequencyVectorGenerator.getVector(
      annotation.getContentWordList.toSeq.zipAll(Nil, "", 1)
    )
  }


}
