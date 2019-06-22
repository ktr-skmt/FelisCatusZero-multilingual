package us.feliscat.util

import scala.xml.NodeSeq

/**
  * @author K. Sakamoto
  *         Created on 2017/05/24
  */
package object xml {
  implicit class XmlUtils(val repr: NodeSeq) extends AnyVal {

    def attrFilter(name: String, value: String): NodeSeq = {
      repr filter (_ \ ("@" + name) exists (_.text == value))
    }

    def \\@(nodeName: String, attrName: String, value: String): NodeSeq = {
      (repr \\ nodeName).attrFilter(attrName, value)
    }

    def \@(nodeName: String, attrName: String, value: String): NodeSeq = {
      (repr \ nodeName).attrFilter(attrName, value)
    }
  }
}
