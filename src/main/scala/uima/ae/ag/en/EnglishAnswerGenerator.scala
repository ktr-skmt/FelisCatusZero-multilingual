package uima.ae.ag.en

import org.apache.uima.jcas.JCas
import uima.ae.ag.MultiLingualAnswerGenerator
import us.feliscat.answer.MultiLingualAnswerCandidatesGenerator
import us.feliscat.answer.en.{EnglishAnswerCandidate, EnglishAnswerCandidates, EnglishAnswerCandidatesGenerator}
import us.feliscat.m17n.English
import us.feliscat.sentence.MultiLingualSentenceGroup
import us.feliscat.sentence.en.{EnglishSentenceCombination, EnglishSentenceCombinationGenerator, EnglishSentenceGroup}
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.types._
import us.feliscat.util.uima.FSListUtils._
import us.feliscat.util.uima.FeatureStructure
import us.feliscat.util.uima.SeqUtils._
import util.Config

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishAnswerGenerator
  extends MultiLingualAnswerGenerator
    with English {
  override protected val mAnswerGenerator: MultiLingualAnswerCandidatesGenerator = {
    new EnglishAnswerCandidatesGenerator(mScoreIndex)
  }

  override protected def groupSentences(keyword: Keyword,
                                        sentences: Seq[Sentence]): MultiLingualSentenceGroup = {
    new EnglishSentenceGroup(keyword, sentences)
  }

  override protected def countNumber(textOpt: StringOption): Int = {
    if (textOpt.isEmpty) {
      return 0
    }
    textOpt.get.split(' ').length
  }

  override def combine(aJCas: JCas,
                       question: Question,
                       scoreIndex: Int,
                       selectedSentenceList: Seq[Sentence],
                       sentenceGroupList: Seq[MultiLingualSentenceGroup]): Unit = {
    //combinations
    val sentenceCombinationGenerator = new EnglishSentenceCombinationGenerator(scoreIndex)
    val sentenceCombinationList: Seq[EnglishSentenceCombination] = {
      sentenceCombinationGenerator.
        generate(
          sentenceGroupList.asInstanceOf[Seq[EnglishSentenceGroup]],
          question.getEndLengthLimit)
    }

    //Maximal Marginal Relevance (MMR) for filling the rest of character limit
    val answerCandidateBuffer = ListBuffer.empty[EnglishSentenceCombination]
    val beginWordLimit: Int = question.getBeginLengthLimit
    val endWordLimit: Int = question.getEndLengthLimit
    sentenceCombinationList foreach {
      sentenceCombination: EnglishSentenceCombination =>
        val wordNumber: Int = sentenceCombination.number
        val restOfWordLimit: Int = endWordLimit - wordNumber
        if (0 < restOfWordLimit) {
          //sort sentences by MMR
          val sentenceMMR: Seq[(Sentence, Double)] = mmrScore(
            sentenceCombination.sentences,
            selectedSentenceList,
            scoreIndex).
            sortWith((a, b) => a._2 > b._2)
          val additionalSentenceBuffer = ListBuffer.empty[Sentence]
          var counter: Int = sentenceCombination.number
          sentenceMMR.map(_._1) foreach {
            s: Sentence =>
              val count: Int = countNumber(StringOption(s.getText))
              if (counter + count <= endWordLimit) {
                counter += count
                additionalSentenceBuffer += s
              }
          }

          if (additionalSentenceBuffer.nonEmpty &&
            beginWordLimit <= counter &&
            counter <= endWordLimit) {
            answerCandidateBuffer += {
              val sc = new EnglishSentenceCombination()
              sc.setSentences(sentenceCombination.sentences ++ additionalSentenceBuffer.result)
              sc.setNumber(counter)
              sc.setScore(sentenceCombination.score)
              sc.setLinkScore(sentenceCombination.linkScore)
              sc.setMergedScores(sentenceCombination.mergedScores)
              sc
            }
          } else if (beginWordLimit <= wordNumber &&
            wordNumber <= endWordLimit) {
            answerCandidateBuffer += sentenceCombination
          } else {
            // Do nothing
          }
        } else if (beginWordLimit <= wordNumber &&
          wordNumber <= endWordLimit) {
          // logically, (beginCharacterLimit <=) characterNumber == endCharacterLimit
          answerCandidateBuffer += sentenceCombination
        } else {
          // Do nothing
        }
    }
    if (0 < answerCandidateBuffer.size) {
      val answerResults: EnglishAnswerCandidates = mAnswerGenerator.
        generate(answerCandidateBuffer.result).asInstanceOf[EnglishAnswerCandidates]
      val topAnswerResult: EnglishAnswerCandidate = answerResults.
        answerResults.
        sortWith((a, b) => a.score > b.score).head.asInstanceOf[EnglishAnswerCandidate]
      topAnswerResult.text match {
        case StringSome(text) =>
          val answer = FeatureStructure.create[Answer]
          answer.setIsGoldStandard(false)
          answer.setWriter(Config.systemName)
          val document = FeatureStructure.create[Document]
          document.setText(text)
          document.setSentenceSet(topAnswerResult.sentenceList.toFSList)
          answer.setDocument(document)
          println("System Answer")
          print("Writer: ")
          println(Config.systemName)
          print("Essay: ")
          println(text)
          val answerSet: Seq[Answer] = question.getAnswerSet.toSeq.asInstanceOf[Seq[Answer]]
          question.setAnswerSet((answerSet :+ answer).toFSList)
        case StringNone =>
          generateAnswerIgnoringLengthLimit(aJCas, question, sentenceCombinationList)
      }
    } else {
      generateAnswerIgnoringLengthLimit(aJCas, question, sentenceCombinationList)
    }
  }

  private def generateAnswerIgnoringLengthLimit(aJCas: JCas, question: Question, sentenceCombinationList: Seq[EnglishSentenceCombination]): Unit = {
    if (0 < sentenceCombinationList.size) {
      val answerResults: EnglishAnswerCandidates = mAnswerGenerator.
        generate(sentenceCombinationList.toList).asInstanceOf[EnglishAnswerCandidates]
      val topAnswerResult: EnglishAnswerCandidate = answerResults.
        answerResults.
        sortWith((a, b) => a.score > b.score).head.asInstanceOf[EnglishAnswerCandidate]
      topAnswerResult.text match {
        case StringSome(text) =>
          val answer = FeatureStructure.create[Answer]
          answer.setIsGoldStandard(false)
          answer.setWriter(Config.systemName)
          val document = FeatureStructure.create[Document]
          document.setText(text)
          document.setSentenceSet(topAnswerResult.sentenceList.toFSList)
          answer.setDocument(document)
          println("System Answer")
          print("Writer: ")
          println(Config.systemName)
          print("Essay: ")
          println(text)
          val answerSet: Seq[Answer] = question.getAnswerSet.toSeq.asInstanceOf[Seq[Answer]]
          question.setAnswerSet((answerSet :+ answer).toFSList)
        case StringNone =>
          generateEmptyAnswer(aJCas, question)
      }
    } else {
      generateEmptyAnswer(aJCas, question)
    }
  }
}