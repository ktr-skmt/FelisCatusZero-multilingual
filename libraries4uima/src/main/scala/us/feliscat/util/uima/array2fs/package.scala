package us.feliscat.util.uima

import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas._

/**
  * @author K. Sakamoto
  *         Created on 2017/05/25
  */
package object array2fs {
  /**
    * @author K.Sakamoto
    * @param repr array
    * @tparam T type
    */
  implicit class ArrayUtils[T <: TOP](repr: Array[T]) extends UimaUtils {
    def toFSArray(implicit id: JCasID): FSArray = {
      val theJCas: JCas = aJCas(id)
      val size: Int = repr.length
      val fsArray = new FSArray(theJCas, size)
      for (i <- 0 until size) {
        fsArray.set(i, repr(i))
      }
      fsArray
    }

    def toFSList(implicit id: JCasID): FSList = {
      val theJCas: JCas = aJCas(id)
      if (repr.isEmpty) {
        //return an empty list
        return new EmptyFSList(theJCas)
      }

      var head = new NonEmptyFSList(theJCas)
      val list: NonEmptyFSList = head
      val it: Iterator[T] = repr.iterator
      while (it.hasNext) {
        head.setHead(it.next)
        if (it.hasNext) {
          head.setTail(new NonEmptyFSList(theJCas))
          head = head.getTail.asInstanceOf[NonEmptyFSList]
        } else {
          head.setTail(new EmptyFSList(theJCas))
        }
      }
      list
    }
  }

  /**
    * @author K.Sakamoto
    * @param repr string array
    */
  implicit class ArrayStringUtils(repr: Array[String]) extends UimaUtils {
    def toStringArray(implicit id: JCasID): StringArray = {
      val theJCas: JCas = aJCas(id)
      val size: Int = repr.length
      val stringArray = new StringArray(theJCas, size)
      for (i <- 0 until size) {
        stringArray.set(i, repr(i))
      }
      stringArray
    }

    def toStringList(implicit id: JCasID): StringList = {
      val theJCas: JCas = aJCas(id)
      if (repr.isEmpty) {
        //return an empty list
        return new EmptyStringList(theJCas)
      }

      var head = new NonEmptyStringList(theJCas)
      val list: NonEmptyStringList = head
      val it: Iterator[String] = repr.iterator
      while (it.hasNext) {
        head.setHead(it.next)
        if (it.hasNext) {
          head.setTail(new NonEmptyStringList(theJCas))
          head = head.getTail.asInstanceOf[NonEmptyStringList]
        } else {
          head.setTail(new EmptyStringList(theJCas))
        }
      }
      list
    }
  }

  /**
    * @author K.Sakamoto
    * @param repr integer array
    */
  implicit class ArrayIntegerUtils(repr: Array[Int]) extends UimaUtils {
    def toIntegerArray(implicit id: JCasID): IntegerArray = {
      val theJCas: JCas = aJCas(id)
      val size: Int = repr.length
      val integerArray = new IntegerArray(theJCas, size)
      for (i <- 0 until size) {
        integerArray.set(i, size)
      }
      integerArray
    }

    def toIntegerList(implicit id: JCasID): IntegerList = {
      val theJCas: JCas = aJCas(id)
      if (repr.isEmpty) {
        //return an empty list
        return new EmptyIntegerList(theJCas)
      }

      var head = new NonEmptyIntegerList(theJCas)
      val list: NonEmptyIntegerList = head
      val it: Iterator[Int] = repr.iterator
      while (it.hasNext) {
        head.setHead(it.next)
        if (it.hasNext) {
          head.setTail(new NonEmptyIntegerList(theJCas))
          head = head.getTail.asInstanceOf[NonEmptyIntegerList]
        } else {
          head.setTail(new EmptyIntegerList(theJCas))
        }
      }
      list
    }
  }

  /**
    * @author K.Sakamoto
    * @param repr float array
    */
  implicit class ArrayFloatUtils(repr: Array[Float]) extends UimaUtils {
    def toFloatArray(implicit id: JCasID): FloatArray = {
      val theJCas: JCas = aJCas(id)
      val size: Int = repr.length
      val floatArray = new FloatArray(theJCas, size)
      for (i <- 0 until size) {
        floatArray.set(i, size)
      }
      floatArray
    }

    def toFloatList(implicit id: JCasID): FloatList = {
      val theJCas: JCas = aJCas(id)
      if (repr.isEmpty) {
        //return an empty list
        return new EmptyFloatList(theJCas)
      }

      var head = new NonEmptyFloatList(theJCas)
      val list: NonEmptyFloatList = head
      val it: Iterator[Float] = repr.iterator
      while (it.hasNext) {
        if (it.hasNext) {
          head.setTail(new NonEmptyFloatList(theJCas))
          head = head.getTail.asInstanceOf[NonEmptyFloatList]
        } else {
          head.setTail(new EmptyFloatList(theJCas))
        }
      }
      list
    }
  }
}
