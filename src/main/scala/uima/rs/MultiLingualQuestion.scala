package uima.rs

import org.apache.uima.jcas.JCas
import us.feliscat.m17n.MultiLingual
import us.feliscat.types.Question
import us.feliscat.util.uima.JCasID

/**
  * <pre>
  * Created on 2017/03/21.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualQuestion(val casId: JCasID,
                                    val aJCas: JCas,
                                    val question: Question) extends MultiLingual
