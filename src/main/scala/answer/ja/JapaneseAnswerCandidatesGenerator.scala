package answer.ja

import answer.MultiLingualAnswerCandidatesGenerator
import jeqa.types.Sentence
import m17n.Japanese
import sentence.MultiLingualSentenceCombination
import sentence.ja.JapaneseSentenceCombination
import text.StringOption
import time.TimeSorter

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseAnswerCandidatesGenerator(scoreIndex: Int)
  extends MultiLingualAnswerCandidatesGenerator(scoreIndex) with Japanese {
  def generate(sentenceCombinationSet: Seq[MultiLingualSentenceCombination]): JapaneseAnswerCandidates = {
    val buffer = ListBuffer.empty[JapaneseAnswerCandidate]
    sentenceCombinationSet foreach {
      case sentenceCombination: JapaneseSentenceCombination =>
        var score: Double = 0D
        val sentences: Seq[Sentence] = sentenceCombination.sentences
        sentenceCombination.sentences foreach {
          sentence: Sentence =>
            score += sentence.getScoreList(scoreIndex).getScore
        }
        score /= sentences.size
        val builder = new StringBuilder(sentenceCombination.number)
        val sentenceList: Seq[Sentence] = TimeSorter.sort(sentences)
        sentenceList foreach {
          sentence: Sentence =>
            builder.append(sentence.getText)
        }
        buffer += new JapaneseAnswerCandidate(score, StringOption(builder.result), sentenceList)
      case _ =>
        // Do nothing
    }
    new JapaneseAnswerCandidates(buffer.result.sortWith((a, b) => a.score > b.score))
  }
}
