package us.feliscat.util

import java.util.stream.Collectors

import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * <pre>
 * Created on 6/3/15.
 * </pre>
 * @author K.Sakamoto
 */
package object json {
  implicit class Elements4Scala(val that: Elements) extends AnyVal {
    def toElementArray: Array[Element] = {
      val list: java.util.List[Element] = that.stream.collect(Collectors.toList[Element])
      list.toArray(new Array[Element](list.size))
    }
  }

}