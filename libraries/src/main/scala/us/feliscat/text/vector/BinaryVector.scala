package us.feliscat.text.vector

import scala.collection.mutable

/**
  * @author K.Sakamoto
  *         Created on 2016/05/22
  */
class BinaryVector(var vector: Seq[String]) extends Vector {
  vector = vector.distinct

  def toFrequencyVector: FrequencyVector = {
    val map = mutable.Map.empty[String, Int]
    vector foreach {
      term: String =>
        map(term) = 1
    }
    new FrequencyVector(map)
  }

  def sum: Long = {
    vector.size
  }

  def innerProduct(v2: BinaryVector): Long = {
    vector.intersect(v2.vector).size
  }
}
