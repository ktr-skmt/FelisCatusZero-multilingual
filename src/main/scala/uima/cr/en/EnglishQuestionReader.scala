package uima.cr.en

import java.io.File

import org.apache.uima.jcas.JCas
import org.apache.uima.resource.metadata.MetaDataObject
import uima.cr.MultiLingualQuestionReader
import uima.rs.MultiLingualQuestion
import uima.rs.en.EnglishQuestion
import us.feliscat.m17n.English
import us.feliscat.text.analyzer.CoreNLP4English
import us.feliscat.text.normalizer.en.{EnglishPunctuations, EnglishStopWords}
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.types.Question
import us.feliscat.util.primitive._
import us.feliscat.util.uima.JCasID

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import scala.xml.NodeSeq

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishQuestionReader extends MultiLingualQuestionReader with English {
  protected override val lengthLimitBeginRegex: Regex = """no less than ([0-9]+) (?:English )?words""".r
  protected override val lengthLimitEndRegex:   Regex = """no more than ([0-9]+) (?:English )?words""".r
  protected override val lengthLimitApproximatorRegex: Regex = """(?:about|approximately) ([0-9]+) (?:English )?words""".r
  protected override val concisenessSet: Set[String] = Set[String](
    "concisely",
    "briefly",
    "in brief",
    "shortly",
    "in short"
  )

  override def read(metaData: java.util.Collection[MetaDataObject],
                    baseDirOpt: StringOption,
                    examFiles: Seq[File]): Seq[MultiLingualQuestion] = {
    multiCAS(metaData, baseDirOpt, examFiles) map {
      case (casId: JCasID, aJCas: JCas, question: Question) =>
        new EnglishQuestion(casId, aJCas, question)
    }
  }

  override def extractInstruction(answerSection: NodeSeq): StringOption = {
    val builder = new StringBuilder()
    answerSection \ "instruction" \ "p" foreach {
      p: NodeSeq =>
        StringOption(p.text.trim) match {
          case StringSome(line) =>
            var instructionOpt = StringOption(line)
            val answerSpaceRegex: Regex = """[Ii]n answer space \(A\)[,\.\s]""".r
            val lengthLimitRegex: Regex = """(?:[Ll]imit your answer to|[Ii]n) (?:[0-9]+) (?:English\swords|lines) or less[,\.]""".r
            val keywordUsageRegex: Regex = """Use each of the .*, and underline each term when it is used\.""".r
            val writeAnswerRegex: Regex = """[Ww]rite your answer""".r
            instructionOpt = instructionOpt map {
              instruction: String =>
                instruction.replaceFirstLiteratim("The terms in parentheses are optional.", "")
            } map {
              instruction: String =>
                answerSpaceRegex.replaceAllIn(instruction, "")
            } map {
              instruction: String =>
                lengthLimitRegex.replaceAllIn(instruction, "")
            } map {
              instruction: String =>
                keywordUsageRegex.replaceAllIn(instruction, "")
            } map {
              instruction: String =>
                writeAnswerRegex.replaceAllIn(instruction, "")
            } map {
              instruction: String =>
                instruction.replaceAllLiteratim("English", "")
            } map {
              instruction: String =>
                instruction.replaceAll("[Ee]xplain", "")
            } map {
              instruction: String =>
                instruction.replaceAll("[Dd]escribe", "")
            } map {
              instruction: String =>
                instruction.replaceAll("[Aa]nswer", "")
            }
            instructionOpt = EnglishStopWords.remove(instructionOpt)
            instructionOpt = EnglishPunctuations.remove(instructionOpt)
            instructionOpt map {
              instruction: String =>
                instruction.replaceAllLiteratim(".", " ")
            }
            val wordsWithPOSes = CoreNLP4English.tagPOS(instructionOpt)

            val buffer = ListBuffer.empty[String]
            wordsWithPOSes filter {
              case (_, pos) =>
                Set[String]("NN", "NNS", "NNP", "JJ").contains(pos) || pos.startsWith("VB")
              case _ =>
                false
            } foreach {
              case (word, _) =>
                buffer += word
              case _ =>
                // Do nothing
            }
            builder.
              append(buffer.
                result.
                distinct.
                mkString(" ")).
              append('\n')
          case StringNone =>
          //Do nothing
        }
    }

    StringOption(
      builder.
        result.
        split(" ").
        distinct.
        mkString(" "))
  }
}
