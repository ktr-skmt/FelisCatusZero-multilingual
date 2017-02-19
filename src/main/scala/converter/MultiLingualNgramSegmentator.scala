package converter

import m17n.MultiLingual
import text.StringOption

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualNgramSegmentator(nGram: Int) extends MultiLingual {
  protected val DELIMITER: String = "\u0020" //" "

  protected def segmentate(elements: Seq[String]): Seq[Array[String]] = {
    val length: Int = elements.length
    val elementArray = new Array[String](length)
    for (i <- 0 until length) {
      elementArray(i) = elements(i)
    }
    val nGramArray = ListBuffer.empty[Array[String]]
    for (i <- 0 to length - nGram) {
      val array: Array[String] = java.util.Arrays.copyOfRange(elementArray, i, i + nGram)
      nGramArray += array
    }
    nGramArray
  }

  protected def merge(segmentatedElements: Seq[Array[String]]): StringOption = {
    val builder = new StringBuilder(calculateRequiredSize(segmentatedElements.length))
    var isFirst: Boolean = true
    segmentatedElements foreach {
      segment: Array[String] =>
        if (isFirst) {
          segment foreach {
            builder.append
          }
          isFirst = false
        } else {
          builder.append(DELIMITER)
          segment foreach {
            builder.append
          }
        }
    }
    StringOption(builder.toString)
  }

  def segmentateWithToken(tokens: Seq[String]): StringOption = {
    merge(segmentate(tokens))
  }

  private def calculateRequiredSize(size: Int): Int = {
    val min: Int = 32
    val ret: Int = (size - nGram) * 2 + 1
    math.max(min, ret)
  }
}
