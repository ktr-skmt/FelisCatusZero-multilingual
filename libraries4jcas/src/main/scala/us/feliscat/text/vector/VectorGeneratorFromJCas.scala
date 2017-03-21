package us.feliscat.text.vector

import us.feliscat.types.{Sentence, TextAnnotation}

/**
  * <pre>
  * Created on 2017/03/19.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait VectorGeneratorFromJCas[V <: Vector] {
  def getVectorFromSentences(sentences: Seq[Sentence]): V

  def getVectorFromAnnotation(annotation: TextAnnotation): V
}
