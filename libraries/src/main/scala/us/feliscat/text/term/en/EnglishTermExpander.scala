package us.feliscat.text.term.en

import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.en.EnglishWordExpressionNormalizer
import us.feliscat.text.term.MultiLingualTermExpander
import us.feliscat.util.StringUtils._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTermExpander extends MultiLingualTermExpander with English {
  private val expansions: Seq[Seq[String]] = {
    val map = mutable.Map.empty[String, ListBuffer[String]]
    EnglishWordExpressionNormalizer.getTerms foreach {
      case (source, target) =>
        if (!map.contains(target)) {
          map(target) = ListBuffer.empty[String]
        }
        map(target) += source
      case _ =>
      // Do nothing
    }
    val buffer = ListBuffer.empty[Seq[String]]
    map.toMap[String, ListBuffer[String]] foreach {
      case (term, expansions_) =>
        expansions_ += term
        buffer += expansions_.result
    }
    buffer.result
  }


  override def expand(termOpt: StringOption): Seq[(String, Boolean)] = {
    if (termOpt.isEmpty) {
      return Nil
    }

    val term: String = termOpt.get

    val expansionBuffer = ListBuffer.empty[(String, Boolean)]
    expansions foreach {
      keywordExpanders: Seq[String] =>
        keywordExpanders foreach {
          keywordExpander: String =>
            (keywordExpanders diff keywordExpander) foreach {
              case k: String if !k.contains(keywordExpander) =>
                expansionBuffer += ((term.replaceAllLiteratim(keywordExpander, k), true))
              case _ =>
              // Do nothing
            }
        }
    }

    expansionBuffer.distinct.result
  }
}
