package uima.rs

import us.feliscat.m17n.MultiLingual
import us.feliscat.types.Question

/**
  * <pre>
  * Created on 2017/03/21.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualQuestion extends MultiLingual {
  var questionOpt: Option[Question] = Option.empty[Question]
}
