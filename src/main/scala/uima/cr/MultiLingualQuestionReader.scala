package uima.cr

import java.io.File
import java.util.Locale
import javax.xml.transform.stream.StreamSource

import modules.util.ModulesConfig
import org.apache.uima.cas.CAS
import org.apache.uima.jcas.JCas
import us.feliscat.exam.essay.xml.Answer
import us.feliscat.m17n.MultiLingual
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.types.{Document, Exam, Keyword, Question, Answer => UAnswer}
import us.feliscat.util.uima.seq2fs.SeqUtils
import us.feliscat.util.uima.{FeatureStructure, JCasUtils}
import us.feliscat.util.xml.XmlSchema

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import scala.util.matching.Regex.Match
import scala.xml.{Node, NodeSeq, XML}

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQuestionReader extends MultiLingual {
  protected var id: Int = 0

  def next(aCAS: CAS, baseDirOpt: StringOption, essayExamFiles: Seq[File]): Unit = {
    if (baseDirOpt.isEmpty) {
      return
    }
    println(s">> ${new Locale(localeId).getDisplayLanguage} Essay Question Reader Processing")
    val aView: JCas = aCAS.createView(localeId).getJCas
    val baseDir: String = baseDirOpt.get
    JCasUtils.setAJCasOpt(Option(aView))
    aView.setDocumentLanguage(localeId)
    val xmlSchema: XmlSchema = ModulesConfig.qaCorpusXmlSchema
    essayExamFiles foreach {
      case file: File if xmlSchema.isValid(new StreamSource(file)) =>
        val exam = FeatureStructure.create[Exam]
        exam.setLabel(file.getName)
        exam.setDir(baseDir)
        exam.setLang(localeId)
        val buffer = ListBuffer.empty[Question]
        val xml: NodeSeq = XML.loadFile(file)
        xml \ "answer_section" foreach {
          answerSection: NodeSeq =>
            val id: StringOption = extractId(answerSection)
            val label: StringOption = extractLabel(answerSection)
            answerSection \ "locale" foreach {
              locale: NodeSeq =>
                val instruction: StringOption = extractInstruction(locale)
                val answerSet: NodeSeq = locale \ "answer_set" \ "answer"
                val localeIdOpt = StringOption((locale \ "@id").text.trim)
                if (id.nonEmpty && instruction.nonEmpty && localeIdOpt.nonEmpty && localeIdOpt.get == localeId) {
                  buffer += getQuestion(
                    aJCas = aView,
                    exam = exam,
                    id = id.get,
                    label = label.getOrElse(""),
                    instruction = instruction.get,
                    lengthLimit = extractLengthLimit(answerSet),
                    keywordSet = extractKeywordSet(locale),
                    answerSet = extractAnswerSet(answerSet),
                    xml = locale)

                }
            }
        }
        exam.setQuestionSet(buffer.result.toFSArray)
      case _ =>
      // Do nothing
    }
  }

  protected def getQuestion(aJCas: JCas,
                            exam: Exam,
                            id: String,
                            label: String,
                            instruction: String,
                            lengthLimit: Range,
                            keywordSet: Seq[String],
                            answerSet: Seq[Answer],
                            xml: NodeSeq): Question = {
    val question = FeatureStructure.create[Question]
    question.setExam(exam)
    question.setBeginLengthLimit(lengthLimit.start)
    question.setEndLengthLimit(lengthLimit.end)
    question.setLabel(label)
    val document = FeatureStructure.create[Document]
    document.setId(id)
    document.setText(instruction)
    question.setDocument(document)
    val keywords: Seq[Keyword] = keywordSet map {
      k: String =>
        val keyword = FeatureStructure.create[Keyword]
        keyword.setIsMandatory(true)
        keyword.setText(k)
        keyword
    }
    question.setKeywordSet(keywords.toFSList)
    val answers: Seq[UAnswer] = answerSet map {
      a: Answer =>
        val answer = FeatureStructure.create[UAnswer]
        answer.setIsGoldStandard(a.isGoldStandard)
        if (a.writer.nonEmpty) {
          answer.setWriter(a.writer.get)
        } else {
          answer.setWriter("(empty)")
        }
        val document = FeatureStructure.create[Document]
        val text: String = a.expression.getOrElse("")
        document.setText(text)
        answer.setDocument(document)
        answer
    }
    question.setAnswerSet(answers.toFSList)
    question.setXml(xml.toString)

    question
  }

  protected def extractId(answerSection: NodeSeq): StringOption = {
    StringOption(Option(answerSection \ "@id").head.text.trim)
    /*
    try {
      Option((answerSection \ "@id").head.us.feliscat.text.toInt)
    } catch {
      case e: Exception =>
        e.printStackTrace()
        None
    }
    */
    //id += 1
    //Option[Int](id)
  }

  protected def extractLabel(answerSection: NodeSeq): StringOption = {
    StringOption((answerSection \ "@label").head.text)
  }

  protected def extractInstruction(answerSection: NodeSeq): StringOption = {
    val builder = new StringBuilder()
    answerSection \ "instruction" \ "p" foreach {
      p: NodeSeq =>
        StringOption(p.text.trim) match {
          case StringSome(line) =>
            builder.
              append(line).
              append('\n')
          case StringNone =>
          //Do nothing
        }
    }
    StringOption(builder.result)
  }

  protected def extractKeywordSet(answerSection: NodeSeq): Seq[String] = {
    val buffer = ListBuffer.empty[String]
    answerSection \ "keyword_set" \ "keyword" foreach {
      keyword: NodeSeq =>
        StringOption(keyword.text.trim) match {
          case StringSome(k) =>
            buffer += k
          case StringNone =>
          //Do nothing
        }
    }
    buffer.result
  }

  protected val lengthLimitBeginRegex:        Regex
  protected val lengthLimitEndRegex:          Regex
  protected val lengthLimitApproximatorRegex: Regex
  protected val concisenessSet: Set[String]

  protected def extractLengthLimit(answerSet: NodeSeq): Range = {
    val answer: Node = answerSet.head
    StringOption((answer \ "@length_limit").text.trim) match {
      case StringSome(lengthLimit) =>
        val begin: Int = getBegin(lengthLimit)
        val end:   Int = getEnd(lengthLimit)
        //begin to end

        //TODO: これを使用しなくて済むように、問題の字数制限を全て「M字以上N字以下」に直す。
        approximateNumberRange(lengthLimit, begin, end)

      case StringNone => 0 to 0
    }
  }

  private def getBegin(lengthLimit: String): Int = {
    getNumber(lengthLimit, lengthLimitBeginRegex, 0)
  }

  private def getEnd(lengthLimit: String): Int = {
    getNumber(lengthLimit, lengthLimitEndRegex, Int.MaxValue)
  }

  private def getNumber(lengthLimit: String, regex: Regex, defaultValue: Int): Int = {
    regex.findFirstMatchIn(lengthLimit) match {
      case Some(m) =>
        try {
          m.group(1).toInt
        } catch {
          case e: NumberFormatException =>
            e.printStackTrace()
            defaultValue
        }
      case None =>
        defaultValue
    }
  }

  protected def approximateNumberRange(lengthLimit: String, begin: Int, end: Int): Range = {
    if ((begin == 0) && (end == Int.MaxValue)) {
      lengthLimitApproximatorRegex.findFirstMatchIn(lengthLimit) match {
        case Some(aboutNumberMatch) =>
          approximateNumberRange(aboutNumberMatch, begin, end)
        case None =>
          if (concisenessSet.contains(lengthLimit)) {
            0 to 100
          } else {
            begin to end
          }
      }
    } else {
      begin to end
    }
  }

  protected def approximateNumberRange(aboutNumberMatch: Match, defaultBegin: Int, defaultEnd: Int): Range = {
    try {
      val aboutNumber: Int = aboutNumberMatch.group(1).toInt
      if (aboutNumber <= 30) {
        0 to (aboutNumber * 2)
      } else {
        (aboutNumber - 30) to (aboutNumber + 30)
      }
    } catch {
      case e: NumberFormatException =>
        e.printStackTrace()
        defaultBegin to defaultEnd
    }
  }

  protected def extractAnswerSet(answerSet: NodeSeq): Seq[Answer] = {
    val expressionSet: NodeSeq = answerSet \ "expression_set" \ "expression"
    expressionSet collect {
      case expression: NodeSeq if StringOption(expression.text.trim).nonEmpty =>
        new Answer(
          isGoldStandard = java.lang.Boolean.parseBoolean((expression \ "@is_gold_standard").text.trim),
          writer         = StringOption((expression \ "@writer").text.trim),
          expression     = StringOption(expression.text.trim)
        )
    }
  }
}
