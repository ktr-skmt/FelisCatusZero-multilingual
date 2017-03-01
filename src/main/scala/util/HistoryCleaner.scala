package util

import java.io.{File => JFile}
import java.nio.file.Paths
import better.files._

/**
  * <pre>
  * Created on 2017/02/22.
  * </pre>
  *
  * @author K.Sakamoto
  */
object HistoryCleaner {
  def clear(): Unit = {
    val resultDir: JFile = Paths.get("out", "result").toFile
    if (resultDir.canRead && resultDir.isDirectory) {
      val dirs: Seq[JFile] = resultDir.listFiles filter {
        dir: JFile =>
          dir.canWrite && dir.isDirectory
      }
      dirs foreach {
        dir: JFile =>
          //somehow the implicit function JFile.toScala is malfunctioning
          //dir.toScala.clear
          //instead of JFile.toScala
          File(dir.toString).delete()
      }
      //...
      val homePage = Paths.get("out", "result", "index.html").toFile
      if (homePage.canWrite) {
        homePage.delete
      }
    }
  }

  def main(args: Array[String]): Unit = {
    clear()
  }
}
