package us.feliscat.sentence.en

import us.feliscat.exam.en.EnglishLengthCounter
import us.feliscat.m17n.English
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
class EnglishSentenceCombinationGenerator(scoreIndex: Int)
  extends MultiLingualSentenceCombinationGenerator(scoreIndex)
    with English {
  private var combos = ListBuffer.empty[EnglishSentenceCombination]

  def generate(input: Seq[EnglishSentenceGroup],
               endingLimit: Int): Seq[EnglishSentenceCombination] = {
    combos = ListBuffer.empty[EnglishSentenceCombination]
    this.endingLimit = endingLimit
    pickUpLimited(
      input,
      input.size - 1,
      new EnglishSentenceCombination()
    )
    if (0 < combos.size) {
      return combos.seq
    }
    pickUpUnlimited(
      input,
      input.size - 1,
      new EnglishSentenceCombination()
    )
    combos.seq
  }

  override protected def count(textOpt: StringOption): Int = {
    EnglishLengthCounter.count(textOpt)
  }

  private def pickUpUnlimited(input: Seq[EnglishSentenceGroup],
                              level: Int,
                              combo: EnglishSentenceCombination): Unit = {
    pickUp(input, level, combo, isLimited = false)
  }

  private def pickUpLimited(input: Seq[EnglishSentenceGroup],
                            level: Int,
                            combo: EnglishSentenceCombination): Unit = {
    pickUp(input, level, combo, isLimited = true)
  }

  private def pickUp(input: Seq[EnglishSentenceGroup],
                     level: Int,
                     combo: EnglishSentenceCombination,
                     isLimited: Boolean): Unit = {
    if (0 <= level) {
      val data: EnglishSentenceGroup = input(level)
      if (0 < data.sentences.size) {
        data.sentences foreach {
          datum: Sentence =>
            if (combo.contains(datum)) {
              val number: Int = combo.number + count(StringOption(datum.getText))
              if (!isLimited || number <= endingLimit) {
                val sc = new EnglishSentenceCombination()
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
