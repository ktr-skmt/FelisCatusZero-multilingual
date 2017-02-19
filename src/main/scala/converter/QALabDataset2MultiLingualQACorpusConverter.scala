package converter

import java.io.{File, PrintWriter}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}
import javax.xml.transform.stream.StreamSource

import util.{Config, XmlSchema}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import scala.xml.{Elem, NodeSeq, PrettyPrinter, XML}

/**
  * <pre>
  * Created on 2017/02/19.
  * </pre>
  *
  * @author K.Sakamoto
  */
object QALabDataset2MultiLingualQACorpusConverter {
  private val filenameRegex: Regex = """(?<qalab>qalab[^-]+)-(?<locale>\w{2,3})-(?<phase>[^-]+)-(?<answerSheetOrGoldStandard>[^-]+)-essay.xml""".r
  private val filenameFormat: String = """%s-%s-answersheet-essay.xml"""

  def convert(): Unit = {
    val goldStandardFileBuffer = ListBuffer.empty[File]
    val answerSheetFileBuffer  = ListBuffer.empty[File]
    val xmlSchema = new XmlSchema(new File(Config.qalabDatasetXmlSchema))
    Paths.get(Config.qalabDatasetPath).toFile.listFiles foreach {
      case file: File if file.canRead && file.isFile && file.getName.endsWith(".xml") && xmlSchema.isValid(new StreamSource(file)) =>
        if (file.getName.contains("goldstandard")) {
          goldStandardFileBuffer += file
        } else if (file.getName.contains("answersheet")) {
          answerSheetFileBuffer += file
        }
      case _ =>
        // Do nothing
    }
    // if gold standard file exists, ignore answer sheet file.
    val goldStandardFileNameList: Seq[String] = goldStandardFileBuffer.result.map(_.getName)
    answerSheetFileBuffer.result foreach {
      answerSheetFile: File =>
        val goldStandardFileName: String = answerSheetFile.getName.replace("answersheet", "goldstandard")
        if (!goldStandardFileNameList.contains(goldStandardFileName)) {
          goldStandardFileBuffer += answerSheetFile
        }
    }
    val fileList: Seq[File] = goldStandardFileBuffer.result
    if (fileList.nonEmpty) {
      println(">> Converting from QA Lab Dataset to Multilingual QA Corpus")
    }

    // file_name -> answer_section (question_id) -> locale
    val fileMap = mutable.LinkedHashMap.empty[String, mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Elem]]]
    val questionIdLabelMap = mutable.Map.empty[String, String]
    fileList foreach {
      file: File =>
        val filename: String = file.getName
        filename match {
          case filenameRegex(qalab, locale, phase, answerSheetOrGoldStandard) =>
            val outputFilename: String = filenameFormat.format(qalab, phase)
            val xml: Elem = XML.loadFile(file)
            if (fileMap.contains(outputFilename)) {
              // answer_section (question_id) -> locale
              val fMap: mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Elem]] = fileMap(outputFilename)
              xml \ "answer_section" foreach {
                answerSection: NodeSeq =>
                  val questionId:    String = (answerSection \ "@id").text.trim
                  val questionLabel: String = (answerSection \ "@label").text.trim
                  if (!questionIdLabelMap.contains(questionId)) {
                    questionIdLabelMap(questionId) = questionLabel
                  }
                  if (fMap.contains(questionId)) {
                    val qMap: mutable.LinkedHashMap[String, Elem] = fMap(questionId)
                    if (!qMap.contains(locale)) {
                      qMap(locale) = getLocale(locale, answerSection, answerSheetOrGoldStandard == "goldstandard")
                    }
                  } else {
                    val qMap = mutable.LinkedHashMap.empty[String, Elem]
                    qMap(locale) = getLocale(locale, answerSection, answerSheetOrGoldStandard == "goldstandard")
                    fMap(questionId) = qMap
                  }
              }
            } else {
              val fMap = mutable.LinkedHashMap.empty[String, mutable.LinkedHashMap[String, Elem]]
              xml \ "answer_section" foreach {
                answerSection: NodeSeq =>
                  val questionId:    String = (answerSection \ "@id").text.trim
                  val questionLabel: String = (answerSection \ "@label").text.trim
                  if (!questionIdLabelMap.contains(questionId)) {
                    questionIdLabelMap(questionId) = questionLabel
                  }
                  if (fMap.contains(questionId)) {
                    val qMap: mutable.LinkedHashMap[String, Elem] = fMap(questionId)
                    if (!qMap.contains(locale)) {
                      qMap(locale) = getLocale(locale, answerSection, answerSheetOrGoldStandard == "goldstandard")
                    }
                  } else {
                    val qMap = mutable.LinkedHashMap.empty[String, Elem]
                    qMap(locale) = getLocale(locale, answerSection, answerSheetOrGoldStandard == "goldstandard")
                    fMap(questionId) = qMap
                  }
              }
              fileMap(outputFilename) = fMap
            }
          case _ =>
            // Do nothing
        }
    }

    fileMap foreach {
      case (fileName, answerSectionMap) =>
        Config.essayExamDirOpt match {
          case Some(dir) =>
            val builder = new StringBuilder()
            builder.append("""<answer_sheet ver="0.1">""")
            answerSectionMap foreach {
              case (questionId, localeMap) =>
                val questionLabel: String = questionIdLabelMap(questionId)
                val localeBuffer = ListBuffer.empty[Elem]
                localeMap foreach {
                  case (_, xml) =>
                    localeBuffer += xml
                  case _ =>
                  /// Do nothing
                }
                builder.append(<answer_section id={questionId} label={questionLabel}>{localeBuffer.result}</answer_section>)
              case _ =>
              // Do nothing
            }
            builder.append("</answer_sheet>")
            val outputPath: Path = Paths.get(dir, fileName)
            val printWriter = new PrintWriter(
              Files.newBufferedWriter(
                outputPath,
                StandardCharsets.UTF_8))
            val printer = new PrettyPrinter(10000, 2)
            printWriter.println("""<?xml version="1.0" encoding="UTF-8"?>""")
            printWriter.print(
              printer.format(
                XML.loadString(
                  builder.result)))
            printWriter.close()
          case None =>
          //
        }
      case _ =>
      // Do nothing
    }
  }

  private def getLocale(locale: String, answerSection: NodeSeq, isGoldStandard: Boolean): Elem = {
    <locale id={locale}>
      {answerSection \ "grand_question_set"}
      {answerSection \ "instruction"}
      {answerSection \ "reference_set"}
      {answerSection \ "blank_question"}
      {answerSection \ "choices"}
      {answerSection \ "keyword_set"}
      {answerSection \ "viewpoint_set"}
      {answerSet(answerSection \ "answer_set", isGoldStandard)}
    </locale>
  }

  private def answerSet(answerSet: NodeSeq, isGoldStandard: Boolean): Elem = {
    val replacementBuffer = ListBuffer.empty[(String, String)]
    answerSet \ "answer" foreach {
      answer: NodeSeq =>
        val expressionBuffer = ListBuffer.empty[String]
        answer \ "expression_set" \ "expression" foreach {
          expression: NodeSeq =>
            expressionBuffer += expression.text.trim
        }
        val expressionArray: Array[String] = expressionBuffer.result.toArray
        for (i <- expressionArray.indices) {
          val expression: String = expressionArray(i)
          replacementBuffer += {
            (
              s"""<expression>[^<]*$expression[^<]*</expression>""",
              s"""<expression writer="writer${i + 1}" is_gold_standard="true">$expression</expression>"""
            )
          }
        }
    }
    var str: String = answerSet.toString filterNot {
      c: Char =>
        c == '\n'
    }
    replacementBuffer.result foreach {
      case (source, target) =>
        str = str.replaceAll(source, target)
    }
    XML.loadString(str)
  }

  def main(args: Array[String]): Unit = {
    convert()
  }
}
