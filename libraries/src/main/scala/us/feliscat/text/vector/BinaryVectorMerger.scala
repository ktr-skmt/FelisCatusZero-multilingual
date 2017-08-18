package us.feliscat.text.vector

/**
  * @author K.Sakamoto
  *         Created on 2016/05/22
  */
object BinaryVectorMerger extends VectorMerger[BinaryVector] {
  override def merge(vectors: Seq[BinaryVector]): BinaryVector = {
    new BinaryVector(
      //Seq[Seq[String]]をflattenするとSeq[Char]になってしまうため、
      //Seq[Seq[String]]を一度List[Set[PseudoString]]を経由してからSeq[String]に変換した。
      {
        for (vector: BinaryVector <- vectors) yield {
          {
            for (term: String <- vector.vector) yield {
              new PseudoString(term)
            }
          }.toSet
        }
      }.toList.flatten.map(_.str)
    )
  }
}

/**
  * @author K.Sakamoto
  * @param str_ string
  */
class PseudoString(str_ : String) {
  def str: String = str_
}