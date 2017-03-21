package us.feliscat.text.vector

/**
  * @author K.Sakamoto
  *         Created on 2016/05/22
  */
trait VectorMerger[V <: Vector] {
  def merge(vectors: Seq[V]): V
}