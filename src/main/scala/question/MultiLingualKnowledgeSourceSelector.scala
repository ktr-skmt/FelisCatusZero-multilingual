package question

import m17n.MultiLingual

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualKnowledgeSourceSelector extends MultiLingual {
  def select(isKeywordQuery: Boolean): Seq[String]
}
