package modules.text.vector.wordembedding.fastText

import java.io.{IOException, PrintWriter}
import java.nio.charset.{CodingErrorAction, StandardCharsets}
import java.nio.file.{Files, Paths}

import modules.util.ModulesConfig
import us.feliscat.text.StringNone

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.sys.process.Process

/**
  * <pre>
  * Created on 2016/12/25.
  * </pre>
  *
  * @author K.Sakamoto
  */
object FastTextVectorExtractor {
  def extract(wordList: Seq[String]): Seq[(String, Array[Float])] = {
    // set up query
    val writer = new PrintWriter(
      Files.newBufferedWriter(
        Paths.get(ModulesConfig.fastTextQuery),
        StandardCharsets.UTF_8))
    try {
      writer.print(wordList.mkString(" "))
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

    // retrieve vectors from query
    val vectorSequenceBuffer = ListBuffer.empty[(String, Array[Float])]

    val command: Seq[String] = Seq[String](
      "fasttext",
      "print-vectors",
      Paths.get(ModulesConfig.fastTextModelBin).toAbsolutePath.toString)

    import us.feliscat.util.process.ProcessBuilderUtils._
    Process(command).#<(Paths.get(ModulesConfig.fastTextQuery).toAbsolutePath.toFile).lineStream(
      StandardCharsets.UTF_8,
      CodingErrorAction.IGNORE,
      CodingErrorAction.IGNORE,
      StringNone
    ) foreach {
      line: String =>
        val tokens: Array[String] = line.split(' ')
        val word: String = tokens.head
        val vectorBuffer = ArrayBuffer.empty[Float]
        vectorBuffer.sizeHint(100)
        tokens.tail foreach {
          token: String =>
            vectorBuffer += {
              try {
                token.toFloat
              } catch {
                case e: NumberFormatException =>
                  e.printStackTrace()
                  0F
              }
            }
        }
        vectorSequenceBuffer += ((word, vectorBuffer.toArray))
    }
    vectorSequenceBuffer.result
  }
}