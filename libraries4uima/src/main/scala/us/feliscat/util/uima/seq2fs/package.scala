package us.feliscat.util.uima

import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas._

/**
  * @author K. Sakamoto
  *         Created on 2017/05/25
  */
package object seq2fs {
  /**
    * @author K.Sakamoto
    * @param repr seq
    * @tparam T type
    */
  implicit class SeqUtils[T <: TOP](repr: Seq[T]) extends UimaUtils {
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

    def toFSArray(implicit id: JCasID): FSArray = {
      val theJCas: JCas = aJCas(id)
      if (repr.isEmpty) {
        //return an empty array
        return new FSArray(theJCas, 0)
      }

      val size: Int = repr.size
      val fsArray = new FSArray(theJCas, size)
      for (i <- 0 until size) {
        fsArray.set(i, repr(i))
      }
      fsArray
    }
  }

  /**
    * @author K.Sakamoto
    * @param repr string seq
    */
  implicit class SeqStringUtils(repr: Seq[String]) extends UimaUtils {
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

    def toStringArray(implicit id: JCasID): StringArray = {
      val theJCas: JCas = aJCas(id)
      if (repr.isEmpty) {
        //return an empty array
        return new StringArray(theJCas, 0)
      }

      val size: Int = repr.size
      val stringArray = new StringArray(theJCas, size)
      for (i <- 0 until size) {
        stringArray.set(i, repr(i))
      }
      stringArray
    }
  }

  /**
    * @author K.Sakamoto
    * @param repr integer seq
    */
  implicit class SeqIntUtils(repr: Seq[Int]) extends UimaUtils {
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

    def toIntegerArray(implicit id: JCasID): IntegerArray = {
      val theJCas: JCas = aJCas(id)
      if (repr.isEmpty) {
        //return an empty array
        return new IntegerArray(theJCas, 0)
      }

      val size: Int = repr.size
      val integerArray = new IntegerArray(theJCas, size)
      for (i <- 0 until size) {
        integerArray.set(i, repr(i))
      }
      integerArray
    }
  }

  /**
    * @author K.Sakamoto
    * @param repr float seq
    */
  implicit class SeqFloatUtils(repr: Seq[Float]) extends UimaUtils {
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
        head.setHead(it.next)
        if (it.hasNext) {
          head.setTail(new NonEmptyFloatList(theJCas))
          head = head.getTail.asInstanceOf[NonEmptyFloatList]
        } else {
          head.setTail(new EmptyFloatList(theJCas))
        }
      }
      list
    }

    def toFloatArray(implicit id: JCasID): FloatArray = {
      val theJCas: JCas = aJCas(id)
      if (repr.isEmpty) {
        //return an empty array
        return new FloatArray(theJCas, 0)
      }

      val size: Int = repr.size
      val floatArray = new FloatArray(theJCas, size)
      for (i <- 0 until size) {
        floatArray.set(i, repr(i))
      }
      floatArray
    }
  }
}
