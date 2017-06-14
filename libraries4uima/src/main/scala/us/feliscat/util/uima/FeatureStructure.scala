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
  def create[T <: TOP : ClassTag](implicit id: JCasID): T = {
    if (JCasUtils.isEmpty || JCasUtils.notContains(id)) {
      throw new Exception()
    }
    val aJCas: JCas = JCasUtils.getAJCas(id)
    val ret: T = implicitly[ClassTag[T]].
      runtimeClass.
      getConstructor(classOf[JCas]).
      newInstance(aJCas).
      asInstanceOf[T]
    ret.addToIndexes()
    ret
  }

  def createArray(size: Int)(implicit id: JCasID): FSArray = {
    if (JCasUtils.isEmpty || JCasUtils.notContains(id)) {
      throw new Exception()
    }
    val aJCas: JCas = JCasUtils.getAJCas(id)
    val ret = new FSArray(aJCas, size)
    ret.addToIndexes()
    ret
  }
}