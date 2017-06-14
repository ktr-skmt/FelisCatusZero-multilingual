package us.feliscat.util.uima

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import org.apache.uima.jcas.JCas

/**
  * @author K.Sakamoto
  *         Created on 2016/09/23
  */
object JCasUtils extends JCasUtils(new ConcurrentHashMap[JCasID, JCas])

/**
  * @author K.Sakamoto
  * @param aJCasMap jCas map
  */
class JCasUtils(aJCasMap: ConcurrentMap[JCasID, JCas]) {
  def setAJCas(aJCas: JCas)(implicit id: JCasID): Unit = {
    if (!contains(id)) {
      aJCasMap.put(id, aJCas)
    }
  }

  def getAJCasOpt(implicit id: JCasID): Option[JCas] = {
    if (contains(id)) {
      Option(aJCasMap.get(id))
    } else {
      None
    }
  }

  def getAJCas(implicit id: JCasID): JCas = {
    if (contains(id)) {
      getAJCasOpt(id).get
    } else {
      throw new Exception()
    }
  }

  def getAJCasMap: ConcurrentMap[JCasID, JCas] = aJCasMap

  def contains(id: JCasID): Boolean = aJCasMap.containsKey(id)

  def notContains(id: JCasID): Boolean = !contains(id)

  def isEmpty: Boolean = aJCasMap.isEmpty

  def nonEmpty: Boolean = !isEmpty
}