package us.feliscat.util.uima

import org.apache.uima.jcas.cas._

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

/**
  * @author K. Sakamoto
  *         Created on 2017/05/25
  */
package object fsList {
  /**
    * @author K.Sakamoto
    * @param repr fs list
    */
  implicit class FSListUtils(val repr: FSList) extends AnyVal {
    def toSeq[T <: TOP : ClassTag]: Seq[T]  = {
      if (Option(repr).isEmpty || repr.isInstanceOf[EmptyFSList]) {
        //return an empty list
        return Nil
      }

      var tail: FSList = repr
      val buffer = ListBuffer.empty[T]
      while (!tail.isInstanceOf[EmptyFSList] || tail.isInstanceOf[NonEmptyFSList]) {
        val nonEmptyFSList: NonEmptyFSList = tail.asInstanceOf[NonEmptyFSList]
        buffer += nonEmptyFSList.getHead.asInstanceOf[T]
        tail = nonEmptyFSList.getTail
      }
      buffer.result
    }

    def toArray[T <: TOP : ClassTag]: Array[T] = {
      val seq: Seq[T] = toSeq
      val array: Array[T] = implicitly[ClassTag[T]].newArray(seq.size)
      for (i <- seq.indices) {
        array(i) = seq(i)
      }
      array
    }
  }

  /**
    * @author K.Sakamoto
    * @param repr float list
    */
  implicit class FloatListUtils(val repr: FloatList) extends AnyVal {
    def toSeq: Seq[Float] = {
      if (Option(repr).isEmpty || repr.isInstanceOf[EmptyFloatList]) {
        //return an empty seq
        return Nil
      }

      var tail: FloatList = repr
      val buffer = ListBuffer.empty[Float]
      while (!tail.isInstanceOf[EmptyFloatList] || tail.isInstanceOf[NonEmptyFloatList]) {
        val nonEmptyFloatList: NonEmptyFloatList = tail.asInstanceOf[NonEmptyFloatList]
        buffer += nonEmptyFloatList.getHead
        tail = nonEmptyFloatList.getTail
      }
      buffer.result
    }

    def toArray: Array[Float] = {
      toSeq.toArray
    }
  }

  /**
    * @author K.Sakamoto
    * @param repr integer list
    */
  implicit class IntegerListUtils(val repr: IntegerList) extends AnyVal {
    def toSeq: Seq[Int] = {
      if (repr == null || repr.isInstanceOf[EmptyIntegerList]) {
        //return an empty seq
        return Nil
      }

      var tail: IntegerList = repr
      val buffer = ListBuffer.empty[Int]
      while (!tail.isInstanceOf[EmptyIntegerList] || tail.isInstanceOf[NonEmptyIntegerList]) {
        val nonEmptyIntegerList: NonEmptyIntegerList = tail.asInstanceOf[NonEmptyIntegerList]
        buffer += nonEmptyIntegerList.getHead
        tail = nonEmptyIntegerList.getTail
      }
      buffer.result
    }

    def toArray: Array[Int] = {
      toSeq.toArray
    }
  }


  /**
    * @author K.Sakamoto
    * @param repr string list
    */
  implicit class StringListUtils(val repr: StringList) extends AnyVal {
    def toSeq: Seq[String] = {
      if (Option(repr).isEmpty || repr.isInstanceOf[EmptyStringList]) {
        //return an empty seq
        return Nil
      }

      var tail: StringList = repr
      val buffer = ListBuffer.empty[String]
      while (!tail.isInstanceOf[EmptyStringList] || tail.isInstanceOf[NonEmptyStringList]) {
        val nonEmptyStringList: NonEmptyStringList = tail.asInstanceOf[NonEmptyStringList]
        buffer += nonEmptyStringList.getHead
        tail = nonEmptyStringList.getTail
      }
      buffer.result
    }

    def toArray: Array[String] = {
      toSeq.toArray
    }
  }
}
