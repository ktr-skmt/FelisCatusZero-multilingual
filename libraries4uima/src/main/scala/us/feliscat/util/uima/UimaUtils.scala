package us.feliscat.util.uima

import org.apache.uima.jcas.JCas

/**
  * @author K. Sakamoto
  *         Created on 2017/05/25
  */
trait UimaUtils {
  protected val aJCas: JCas = JCasUtils.getAJCasOpt match {
    case Some(cas) =>
      cas
    case None =>
      throw new Exception()
  }
}
