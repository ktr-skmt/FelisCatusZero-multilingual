package us.feliscat.util.uima

import org.apache.uima.jcas.JCas

/**
  * @author K. Sakamoto
  *         Created on 2017/05/25
  */
trait UimaUtils {
  protected def aJCas(implicit id: JCasID): JCas = {
    JCasUtils.getAJCas(id)
  }
}
