package us.feliscat.answer.en

import us.feliscat.answer.MultiLingualAnswerCandidatesGenerator
import us.feliscat.m17n.English
import us.feliscat.sentence.MultiLingualSentenceCombination
import us.feliscat.sentence.en.EnglishSentenceCombination
import us.feliscat.text.StringOption
import us.feliscat.time.TimeSorter
import us.feliscat.types.Sentence

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishAnswerCandidatesGenerator(scoreIndex: Int)
  extends MultiLingualAnswerCandidatesGenerator(scoreIndex) with English {
  def generate(sentenceCombinationSet: Seq[MultiLingualSentenceCombination]): EnglishAnswerCandidates = {
    val buffer = ListBuffer.empty[EnglishAnswerCandidate]
    sentenceCombinationSet foreach {
      case sentenceCombination: EnglishSentenceCombination =>
        var score: Double = 0D
        val sentences: Seq[Sentence] = sentenceCombination.sentences
        sentenceCombination.sentences foreach {
          sentence: Sentence =>
            score += sentence.getScoreList(scoreIndex).getScore
        }
        score /= sentences.size
        val builder = new StringBuilder()
        val sentenceList: Seq[Sentence] = TimeSorter.sort(sentences)
        sentenceList foreach {
          sentence: Sentence =>
            builder.append(sentence.getText)
        }
        buffer += new EnglishAnswerCandidate(score, StringOption(builder.result), sentenceList)
      case _ =>
        // Do nothing
    }
    new EnglishAnswerCandidates(buffer.result.sortWith((a, b) => a.score > b.score))
  }
}
