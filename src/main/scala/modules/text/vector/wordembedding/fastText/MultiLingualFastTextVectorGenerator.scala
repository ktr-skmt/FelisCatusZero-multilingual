package modules.text.vector.wordembedding.fastText

import java.io.{File, IOException, PrintWriter}
import java.nio.charset.{CodingErrorAction, StandardCharsets}
import java.nio.file.{Files, Path, Paths}

import modules.text.vector.wordembedding.MultiLingualWordEmbeddingGenerator
import modules.util.ModulesConfig
import us.feliscat.ir.fulltext.indri.IndriResult
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.util.LibrariesConfig
import us.feliscat.util.process._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.io.Source
import scala.sys.process.Process
import scala.xml.{NodeSeq, XML}

/**
  * <pre>
  * Created on 2016/12/24.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualFastTextVectorGenerator extends MultiLingualWordEmbeddingGenerator {
  protected val fastTextResource: String
  protected val fastTextModel: String
  protected val trecTextFormatData: Seq[String]

  protected def toIndriResultMap(
                       lines: Iterator[String],
                       keywordOriginalTextOpt: StringOption,
                       expansionOnlyList: Seq[String],
                       indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult]

  protected def extractWords(sentences: StringOption): Seq[String]

  @throws[Exception]
  override def main(args: Array[String]): Unit = {
    println(">> fastText Word Vector Generating")
    val indriResultMap = mutable.Map.empty[String, IndriResult]
    trecTextFormatData foreach {
      path: String =>
        //println(path.toString)
        Paths.get(path).toFile.listFiles foreach {
          file: File =>
            indriResultMap ++=
              toIndriResultMap(Source.fromFile(file).getLines, StringNone, Nil, indriResultMap)
        }
    }

    val builder = new StringBuilder()
    indriResultMap foreach {
      case (_, indriResult) =>
        indriResult.text match {
          case StringSome(t) =>
            builder.append(t)
          case StringNone =>
          // Do nothing
        }
    }

    val mEssayExamFiles = ListBuffer.empty[File]
    ModulesConfig.essayExamDirOpt match {
      case Some(essayExamDir) =>
        val essayExamDirFile = new File(essayExamDir)
        if (essayExamDirFile.canRead && essayExamDirFile.isDirectory) {
          essayExamDirFile.listFiles foreach {
            case file: File if file.canRead && file.isFile && file.getName.endsWith(".xml") =>
              mEssayExamFiles += file
            case _ =>
            // Do nothing
          }
        }
      case None =>
      // Do nothing
    }
    mEssayExamFiles.result foreach {
      file: File =>
        //println(file.getName)
        val xml: NodeSeq = XML.loadFile(file)
        xml \ "answer_section" foreach {
          answerSection: NodeSeq =>
            answerSection \ "instruction" \ "p" foreach {
              p: NodeSeq =>
                //println(p.text.trim)
                builder.append(p.text.trim)
            }
            builder.append ({
              for (keyword: NodeSeq <- answerSection \ "keyword_set" \ "keyword") yield {
                keyword.text.trim
              }
            }.mkString(" ", " ", " "))
            val answerSet: NodeSeq = answerSection \ "answer_set" \ "answer"
            answerSet foreach {
              answer: NodeSeq =>
                answer \ "expression_set"  \ "expression" foreach {
                  expression: NodeSeq =>
                    //println(expression.text.trim)
                    builder.append(expression.text.trim)
                }
            }
        }
    }

    val dataPath: Path = Paths.get(fastTextResource)
    val writer = new PrintWriter(
      Files.newBufferedWriter(dataPath, StandardCharsets.UTF_8))
    try {
      extractWords(StringOption(builder.result)) foreach {
        word: String =>
          writer.print(word.concat(" "))
      }
    } catch {
      case e: IOException =>
        e.printStackTrace()
    } finally {
      if (Option(writer).nonEmpty) {
        try {
          writer.close()
        } catch {
          case e: IOException =>
            e.printStackTrace()
        }
      }
    }

    val modelPath: Path = Paths.get(fastTextModel)
    val command = Seq[String](
      "fasttext",
      "skipgram",
      "-minCount", LibrariesConfig.fastTextGeneratorMinimumFrequency.toString,
      "-input", dataPath.toAbsolutePath.toString,
      "-output", modelPath.toAbsolutePath.toString
    )

    Process(command).lineStream(
      StandardCharsets.UTF_8,
      CodingErrorAction.IGNORE,
      CodingErrorAction.IGNORE,
      StringNone,
      LibrariesConfig.fastTextGeneratorTimeout.minute
    ) foreach println
  }
}