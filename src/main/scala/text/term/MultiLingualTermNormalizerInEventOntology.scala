package text.term

import java.util.regex.Pattern

import m17n.MultiLingual
import text.{StringNone, StringOption}
import util.Config

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualTermNormalizerInEventOntology extends TermNormalizer with MultiLingual {
  override def normalize(textOpt: StringOption): StringOption = {
    if (textOpt.isEmpty) {
      return StringNone
    }
    val text: String = textOpt.get
    Config.useTermNormalizer = false
    recognize(textOpt).foreach {
      case namedEntity if namedEntity.textOpt.nonEmpty && 1 < namedEntity.synonyms.size =>
        val ne: String = namedEntity.textOpt.get
        var minLengthSynonym: String = ne
        namedEntity.synonyms foreach {
          case synonym if synonym.length < minLengthSynonym.length =>
            minLengthSynonym = synonym
          case _ =>
          //Do nothing
        }
        text.replaceAll(Pattern.quote(ne), minLengthSynonym)
      case _ =>
      //Do nothing
    }
    Config.useTermNormalizer = true
    StringOption(text)
  }
}
