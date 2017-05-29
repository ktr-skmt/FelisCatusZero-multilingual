package us.feliscat.util.uima

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
    def toFSList: FSList = {
      if (repr.isEmpty) {
        //return an empty list
        return new EmptyFSList(aJCas)
      }

      var head = new NonEmptyFSList(aJCas)
      val list: NonEmptyFSList = head
      val it: Iterator[T] = repr.iterator
      while (it.hasNext) {
        head.setHead(it.next)
        if (it.hasNext) {
          head.setTail(new NonEmptyFSList(aJCas))
          head = head.getTail.asInstanceOf[NonEmptyFSList]
        } else {
          head.setTail(new EmptyFSList(aJCas))
        }
      }
      list
    }

    def toFSArray: FSArray = {
      if (repr.isEmpty) {
        //return an empty array
        return new FSArray(aJCas, 0)
      }

      val size: Int = repr.size
      val fsArray = new FSArray(aJCas, size)
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
    def toStringList: StringList = {
      if (repr.isEmpty) {
        //return an empty list
        return new EmptyStringList(aJCas)
      }

      var head = new NonEmptyStringList(aJCas)
      val list: NonEmptyStringList = head
      val it: Iterator[String] = repr.iterator
      while (it.hasNext) {
        head.setHead(it.next)
        if (it.hasNext) {
          head.setTail(new NonEmptyStringList(aJCas))
          head = head.getTail.asInstanceOf[NonEmptyStringList]
        } else {
          head.setTail(new EmptyStringList(aJCas))
        }
      }
      list
    }

    def toStringArray: StringArray = {
      if (repr.isEmpty) {
        //return an empty array
        return new StringArray(aJCas, 0)
      }

      val size: Int = repr.size
      val stringArray = new StringArray(aJCas, size)
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
    def toIntegerList: IntegerList = {
      if (repr.isEmpty) {
        //return an empty list
        return new EmptyIntegerList(aJCas)
      }

      var head = new NonEmptyIntegerList(aJCas)
      val list: NonEmptyIntegerList = head
      val it: Iterator[Int] = repr.iterator
      while (it.hasNext) {
        head.setHead(it.next)
        if (it.hasNext) {
          head.setTail(new NonEmptyIntegerList(aJCas))
          head = head.getTail.asInstanceOf[NonEmptyIntegerList]
        } else {
          head.setTail(new EmptyIntegerList(aJCas))
        }
      }
      list
    }

    def toIntegerArray: IntegerArray = {
      if (repr.isEmpty) {
        //return an empty array
        return new IntegerArray(aJCas, 0)
      }

      val size: Int = repr.size
      val integerArray = new IntegerArray(aJCas, size)
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
    def toFloatList: FloatList = {
      if (repr.isEmpty) {
        //return an empty list
        return new EmptyFloatList(aJCas)
      }

      var head = new NonEmptyFloatList(aJCas)
      val list: NonEmptyFloatList = head
      val it: Iterator[Float] = repr.iterator
      while (it.hasNext) {
        head.setHead(it.next)
        if (it.hasNext) {
          head.setTail(new NonEmptyFloatList(aJCas))
          head = head.getTail.asInstanceOf[NonEmptyFloatList]
        } else {
          head.setTail(new EmptyFloatList(aJCas))
        }
      }
      list
    }

    def toFloatArray: FloatArray = {
      if (repr.isEmpty) {
        //return an empty array
        return new FloatArray(aJCas, 0)
      }

      val size: Int = repr.size
      val floatArray = new FloatArray(aJCas, size)
      for (i <- 0 until size) {
        floatArray.set(i, repr(i))
      }
      floatArray
    }
  }
}
