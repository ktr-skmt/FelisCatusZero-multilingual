package us.feliscat.util.uima

import org.apache.uima.jcas.JCas

import scala.beans.BeanProperty

/**
  * @author K.Sakamoto
  *         Created on 2016/09/23
  */
object JCasUtils extends JCasUtils(None)

/**
  * @author K.Sakamoto
  * @param aJCasOpt jCas option
  */
class JCasUtils(@BeanProperty var aJCasOpt: Option[JCas] = None)
