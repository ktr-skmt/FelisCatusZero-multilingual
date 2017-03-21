package us.feliscat.text.vector

import scala.collection.mutable

/**
  * @author K.Sakamoto
  *         Created on 2016/05/22
  */
trait VectorGenerator[V <: Vector] {
  private val cache = mutable.Map.empty[Long, V]

  def getVectorFromCache(id: Long, sentence: String): V = {
    if (cache contains id) {
      cache(id)
    } else {
      val vector: V = getVectorFromSentence(sentence)
      cache(id) = vector
      vector
    }
  }

  def getVectorFromText(text: String): V

  def getVectorFromSentence(sentence: String): V
}
