package us.feliscat.ir.fulltext.indri

import java.nio.charset.{CodingErrorAction, StandardCharsets}
import java.nio.file.Path

import us.feliscat.text.StringNone
import us.feliscat.util.LibrariesConfig
import us.feliscat.util.process._

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.sys.process.Process

/**
  * <pre>
  * Created on 2017/01/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
class IndriIndexer(inputPath: Path, indexPath: Path) {
  private def command: Seq[String] = {
    "IndriBuildIndex" ::
    "-field.name=TITLE" ::
    "-memory=".concat(LibrariesConfig.indriMemory) ::
    "-corpus.path=".concat(inputPath.toAbsolutePath.toString) ::
    "-corpus.class=trectext" ::
    "-index=".concat(indexPath.toAbsolutePath.toString) :: Nil
  }

  def index(): Unit = {
    val buffer = ListBuffer.empty[String]
    command.foreach(buffer.+=)
    Process(buffer.result).lineStream(
        StandardCharsets.UTF_8,
        CodingErrorAction.IGNORE,
        CodingErrorAction.IGNORE,
        StringNone,
        LibrariesConfig.indriBuildIndexTimeout.minute
      ).foreach(println)
  }
}
