package us.feliscat.util.uima

import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.{FSArray, TOP}

import scala.reflect.ClassTag

/**
  * <pre>
  * Created on 2017/03/02.
  * </pre>
  *
  * @author K.Sakamoto
  */
object FeatureStructure {
  def create[T <: TOP](implicit tag: ClassTag[T]): T = {
    if (JCasUtils.aJCasOpt.isEmpty) {
      throw new Exception()
    }
    val aJCas: JCas = JCasUtils.aJCasOpt.get
    val ret: T = tag.runtimeClass.
      getConstructor(classOf[JCas]).
      newInstance(aJCas).
      asInstanceOf[T]
    ret.addToIndexes()
    ret
  }

  def createArray(size: Int): FSArray = {
    if (JCasUtils.aJCasOpt.isEmpty) {
      throw new Exception()
    }
    val aJCas: JCas = JCasUtils.aJCasOpt.get
    val ret = new FSArray(aJCas, size)
    ret.addToIndexes()
    ret
  }
}