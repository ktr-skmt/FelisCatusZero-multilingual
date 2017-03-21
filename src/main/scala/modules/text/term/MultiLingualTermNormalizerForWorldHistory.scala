package modules.text.term

import us.feliscat.m17n.MultiLingual
import us.feliscat.ner.NamedEntity
import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualTermNormalizerForWorldHistory extends TermNormalizer with MultiLingual {
  override def recognize(textOpt: StringOption): Seq[NamedEntity] = Seq.empty[NamedEntity]
}
