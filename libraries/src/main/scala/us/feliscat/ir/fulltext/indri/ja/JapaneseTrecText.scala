package us.feliscat.ir.fulltext.indri.ja

import us.feliscat.ir.fulltext.indri.MultiLingualTrecText
import us.feliscat.m17n.Japanese
import us.feliscat.text.StringOption

import scala.collection.mutable.ListBuffer
import scala.xml.NodeSeq

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseTrecText extends MultiLingualTrecText with Japanese {

  override protected def getTitleOpt(doc: NodeSeq): StringOption = {
    removeSpace(StringOption((doc \ "title").text.trim))
  }

  override protected def getTextOpt(doc: NodeSeq): StringOption = {
    removeSpace(StringOption((doc \ "text").text.trim))
  }

  private def removeSpace(text: StringOption): StringOption = {
    text map {
      t: String =>
        val charBuffer = ListBuffer.empty[Char]
        t.trim.foreach {
          case ch if ch != ' ' =>
            charBuffer += ch
          case _ =>
          // Do nothing
        }
        new String(charBuffer.result.toArray)
    }
  }
}
