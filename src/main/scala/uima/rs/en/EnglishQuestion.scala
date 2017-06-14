package uima.rs.en

import org.apache.uima.jcas.JCas
import uima.rs.MultiLingualQuestion
import us.feliscat.m17n.English
import us.feliscat.types.Question
import us.feliscat.util.uima.JCasID

/**
  * <pre>
  * Created on 2017/03/21.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishQuestion(casId: JCasID,
                      aJCas: JCas,
                      question: Question) extends MultiLingualQuestion(casId, aJCas, question) with English