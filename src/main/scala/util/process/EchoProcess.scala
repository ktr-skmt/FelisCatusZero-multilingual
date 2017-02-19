package util.process

import text.normalizer.ja.JapaneseNormalizedString
import text.{StringNone, StringSome}

import scala.sys.process.{Process, ProcessBuilder}

/**
  * Created by nakayama.
  * @author Nakayama
  */
trait EchoProcess {
  def echo(str: String): ProcessBuilder = {
    Process(Seq("/usr/bin/printf", str.toString))
  }

  def echo(sentence: JapaneseNormalizedString): ProcessBuilder = {
    sentence.toStringOption match {
      case StringSome(s) =>
        echo(s.toString)
      case StringNone =>
        Process(Nil)
    }
  }
}