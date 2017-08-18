package us.feliscat.text.vector.wordembedding

import us.feliscat.m17n.MultiLingual

/**
  * @author K. Sakamoto
  *         Created on 2017/07/13
  */
trait MultiLingualWordEmbeddingExtractor extends MultiLingual {
  def extract(wordList: Seq[String]): Seq[(String, Array[Float])]
}
