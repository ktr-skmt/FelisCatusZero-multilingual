package us.feliscat.sentence.ja

import us.feliscat.exam.ja.JapaneseLengthCounter
import us.feliscat.m17n.Japanese
import us.feliscat.sentence.MultiLingualSentenceCombinationGenerator
import us.feliscat.text.StringOption
import us.feliscat.types.Sentence

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseSentenceCombinationGenerator(scoreIndex: Int)
  extends MultiLingualSentenceCombinationGenerator(scoreIndex)
    with Japanese {
  private var combos = ListBuffer.empty[JapaneseSentenceCombination]

  def generate(input: Seq[JapaneseSentenceGroup],
               endingLimit: Int): Seq[JapaneseSentenceCombination] = {
    combos = ListBuffer.empty[JapaneseSentenceCombination]
    this.endingLimit = endingLimit
    pickUpLimited(
      input,
      input.size - 1,
      new JapaneseSentenceCombination()
    )
    if (0 < combos.size) {
      return combos.seq
    }
    pickUpUnlimited(
      input,
      input.size - 1,
      new JapaneseSentenceCombination()
    )
    combos.seq
  }

  override protected def count(textOpt: StringOption): Int = {
    JapaneseLengthCounter.count(textOpt)
  }

  private def pickUpUnlimited(input: Seq[JapaneseSentenceGroup],
                              level: Int,
                              combo: JapaneseSentenceCombination): Unit = {
    pickUp(input, level, combo, isLimited = false)
  }

  private def pickUpLimited(input: Seq[JapaneseSentenceGroup],
                            level: Int,
                            combo: JapaneseSentenceCombination): Unit = {
    pickUp(input, level, combo, isLimited = true)
  }

  private def pickUp(input: Seq[JapaneseSentenceGroup],
                     level: Int,
                     combo: JapaneseSentenceCombination,
                     isLimited: Boolean): Unit = {
    if (0 <= level) {
      val data: JapaneseSentenceGroup = input(level)
      if (0 < data.sentences.size) {
        data.sentences foreach {
          datum: Sentence =>
            if (combo.contains(datum)) {
              val number: Int = combo.number + count(StringOption(datum.getText))
              if (!isLimited || number <= endingLimit) {
                val sc = new JapaneseSentenceCombination()
                sc.setSentences(combo.sentences :+ datum)
                sc.setNumber(number)
                sc.setScore(combo.score + datum.getScoreList(scoreIndex).getScore)
                pickUp(
                  input,
                  level - 1,
                  sc,
                  isLimited
                )
              }
            } else {
              pickUp(
                input,
                level - 1,
                combo,
                isLimited
              )
            }
        }
      } else {
        pickUp(
          input,
          level - 1,
          combo,
          isLimited
        )
      }
    } else {
      combos += combo
    }
  }
}
