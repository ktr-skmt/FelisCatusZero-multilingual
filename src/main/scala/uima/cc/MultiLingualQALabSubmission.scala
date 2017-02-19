package uima.cc

import java.io.File
import java.nio.file.{Path, Paths}
import java.util.Locale

import m17n.MultiLingual
import org.apache.uima.cas.CAS
import org.apache.uima.jcas.JCas
import text.{StringNone, StringOption, StringSome}
import util.Config
import util.uima.JCasUtils

/**
  * <pre>
  * Created on 2017/02/14.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQALabSubmission extends MultiLingual {
  protected val mSubtaskOpt: StringOption
  protected var mTeamIdOpt:  StringOption = Config.teamIdOpt
  protected var mRunIdOpt:   Option[Int]  = Config.runIdOpt

  protected def subtask: String = {
    val whatSubtask: String = "what-subtask"

    mSubtaskOpt match {
      case StringSome(subtask) =>
        subtask.toLowerCase match {
          case "extraction subtask" =>
            "extraction"
          case "summarization subtask" =>
            "summarization"
          case "evaluation method subtask" =>
            "evaluation-method"
          case _ =>
            whatSubtask
        }
      case StringNone =>
        whatSubtask
    }
  }

  protected def teamId: String = {
    mTeamIdOpt match {
      case StringSome(teamId) =>
        teamId
      case StringNone =>
        "Team-ID"
    }
  }

  protected def runId: String = {
    mRunIdOpt match {
      case Some(runId) =>
        f"$runId%02d"
      case None =>
        "01"
    }
  }

  def getOutputFilePath(examFileName: String): Path = {
    val dirPath: Path = Paths.get("out", "qalab")
    val dirFile: File = dirPath.toFile
    if (!dirFile.canRead) {
      dirFile.mkdir
    }
    if (!dirFile.isDirectory) {
      dirFile.delete
      dirFile.mkdir
    }
    val builder = new StringBuilder()
    builder.
      append(examFileName.dropRight(4)).
      append('_').
      append(teamId).
      append('_').
      append(subtask).
      append('_').
      append(runId).
      append(".xml")
    Paths.get("out", "qalab", builder.result)
  }

  def process(aCAS: CAS): Unit = {
    println(s">> ${new Locale(localeId).getDisplayLanguage} QA Lab $subtask Cas Consumer Processing")
    val aView: JCas = aCAS.getView(localeId).getJCas
    JCasUtils.setAJCasOpt(Option(aView))
    process(aView)
  }

  protected def process(aJCas: JCas): Unit
}
