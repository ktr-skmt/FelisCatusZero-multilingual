package uima.cr

import java.io.{File, IOException}
import java.nio.file.Paths

import jeqa.types.{Answer => UAnswer}
import org.apache.uima.cas.{CAS, CASException}
import org.apache.uima.collection.{CollectionException, CollectionReader_ImplBase}
import org.apache.uima.resource.ResourceInitializationException
import org.apache.uima.util.Progress
import text.StringOption
import uima.cr.en.EnglishEssayQuestionReader
import uima.cr.ja.JapaneseEssayQuestionReader
import util.Config

import scala.collection.mutable.ListBuffer

/**
 * <p>論述問題をファイルから読み込むプログラム</p>
 * @author K.Sakamoto
 *         Created on 15/10/30
 */
class EssayQuestionReader extends CollectionReader_ImplBase {
  private var mHasNextFlag = false
  private var mEssayExamDir = Option.empty[String]
  private val mEssayExamFiles = ListBuffer.empty[File]

  @throws[ResourceInitializationException]
  override def initialize(): Unit = {
    println(">> Essay Question Reader Initializing")
    super.initialize()
    mHasNextFlag = true
    mEssayExamDir = Config.essayExamDirOpt
    mEssayExamDir match {
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
  }

  @throws[IOException]
  @throws[CollectionException]
  override def getNext(aCAS: CAS): Unit = {
    println(">> Essay Question Reader Processing")

    try {
      if (mEssayExamDir.isEmpty | mEssayExamFiles.isEmpty) {
        return
      }
    } catch {
      case e: CASException =>
        throw new CollectionException(e)
    }
    val baseDirOpt = StringOption(Paths.get(mEssayExamDir.get).toAbsolutePath.toString)
    val essayExamFiles: Seq[File] = mEssayExamFiles.result

    JapaneseEssayQuestionReader.next(
      aCAS,
      baseDirOpt,
      essayExamFiles
    )

    EnglishEssayQuestionReader.next(
      aCAS,
      baseDirOpt,
      essayExamFiles
    )

    mHasNextFlag = false
  }

  override def getProgress: Array[Progress] = {
    Array.empty[Progress]
  }

  @throws[IOException]
  @throws[CollectionException]
  override def hasNext: Boolean = {
    mHasNextFlag
  }

  @throws[IOException]
  override def close(): Unit = {
    // Do nothing
  }
}