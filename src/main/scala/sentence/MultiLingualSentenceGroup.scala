package sentence

import jeqa.types.{Keyword, Sentence, Time}
import m17n.MultiLingual
import text.StringOption

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/10.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualSentenceGroup(val keyword: Keyword,
                                         val sentences: Seq[Sentence]) extends MultiLingual {
  private type CountTextTime = (Int, String, Time, Time)

  protected def count(sentenceOpt: StringOption): Int

  def sortedByCharacterCount: (Keyword, List[CountTextTime]) = {
    val buffer = ListBuffer.empty[CountTextTime]
    sentences.foreach {
      sentence: Sentence =>
        val sentenceText: StringOption = normalize(StringOption(sentence.getText))
        buffer += ((count(sentenceText), sentenceText.toString, sentence.getBeginTime, sentence.getEndTime))
    }
    val sort: Seq[CountTextTime] = buffer.result.sortWith((f1, f2) => f1._1 < f2._1)
    val length: Int = sort.length
    val result = new Array[CountTextTime](length)
    for (i <- 0 until length) {
      if (0 < i) {
        result(i) = (sort(i)._1 - sort(i - 1)._1, sort(i)._2, sort(i)._3, sort(i)._4)
      } else if (i == 0) {
        result(i) = (sort(i)._1, sort(i)._2, sort(i)._3, sort(i)._4)
      }
    }
    (keyword, result.toList)
  }

  override def toString: String = {
    """%s
      |Sentences:
      |%s
      |""".stripMargin.format(
      keyword,
      sentencesToString(sentences)
    )
  }

  protected def sentencesToString(sentences: Seq[Sentence]) : String = {
    val builder = new StringBuilder()
    sentences foreach {
      sentence: Sentence =>
        builder.append(sentence.getText)
    }
    builder.result
  }
}
