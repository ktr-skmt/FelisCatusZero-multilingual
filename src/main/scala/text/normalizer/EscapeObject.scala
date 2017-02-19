package text.normalizer

import java.nio.file.Paths

import m17n.MultiLingual
import text.{StringNone, StringOption, StringSome}
import util.Config

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * @author K.Sakamoto
  *         Created on 2016/08/06
  */
abstract class EscapeObject(objectFileNameOpt: StringOption) extends MultiLingual {

  val objects: Seq[String] = initialize()

  private def initialize(): Seq[String] = {
    if (objectFileNameOpt.isEmpty) {
      return Nil
    }

    val buffer = ListBuffer.empty[String]
    Source.fromFile(
      Paths.get(Config.resourcesDir, "normalizer", localeId, objectFileNameOpt.get).toAbsolutePath.toFile
    ).getLines foreach {
      line: String =>
        normalize(StringOption(line.trim)) match {
          case StringSome(l) =>
            buffer += l
          case StringNone =>
            //Do nothing
        }
    }

    buffer.result
  }
}
