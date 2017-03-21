package us.feliscat.text.analyzer.mor.juman

import us.feliscat.text.analyzer.mor.MorphemeAnalyzer
import us.feliscat.text.{StringNone, StringOption, StringSome}

import scala.collection.mutable.ListBuffer
import scala.sys.process.{Process, ProcessBuilder}

/**
  * @author K.Sakamoto
  *         Created on 2016/12/04
  */
object JumanPlusPlus extends MorphemeAnalyzer {
  override def analyzer(): ProcessBuilder = {
    Process("juman++" :: Nil)
  }

  //return originalText, us.feliscat.text, pos
  override def extractMorphemes(sentence: StringOption): Seq[(String, String, String, StringOption, Seq[String])] = {
    Nil
  }

  override val negativePosListForContentWord: Seq[String] =
    "名詞-形式名詞" :: //"の", "こと", "もの", "つもり", "わけ"
    "名詞-副詞的名詞" :: //"ところ", "ため", "ぐらい"
    "助詞" ::
    "助動詞" ::
    "判定詞" ::
    "特殊" ::
    "未定義語-その他" :: Nil

  private def isNoun(pos: StringOption): Boolean = {
    pos match {
      case StringSome(p) =>
        (p startsWith "名詞") &&
          !(p startsWith "名詞-形式名詞") &&
          !(p startsWith "名詞-副詞的名詞")
      case StringNone =>
        false
    }
  }

  override def extractContentWords(sentence: StringOption): Seq[String] = {
    val terms = ListBuffer.empty[String]
    val builder = new StringBuilder(4)

    def extractCompoundNoun(): Unit = {
      if (0 < builder.length) {
        terms += builder.result
        builder.clear
      }
    }

    def compoundNoun(tokens: Seq[String], pos: StringOption): Unit = {
      def bufferNoun(): Unit = {
        builder.append(tokens(2))
      }

      if (isNoun(pos)) {
        bufferNoun()
      } else {
        extractCompoundNoun()
      }
    }

    analyze(sentence) foreach {
      case line if line != "EOS" =>
        val tokens = line.split(" ")
        val pos = tokens(5) match {
          case "*" =>
            tokens(3)
          case otherwise =>
            "%s-%s" format (tokens(3), otherwise)
        }
        compoundNoun(tokens, StringOption(pos))
      case _ =>
    }

    extractCompoundNoun()

    terms.result
  }

  override def extractWords(sentence: StringOption): Seq[String] = {
    val terms = ListBuffer[String]()
    val builder = new StringBuilder(4)

    def extractCompoundNoun(): Unit = {
      if (0 < builder.length) {
        terms += builder.result
        builder.clear
      }
    }

    def compoundNoun(tokens: Seq[String], pos: StringOption): Unit = {
      def bufferNoun(): Unit = {
        builder.append(tokens(2))
      }

      if (isNoun(pos)) {
        bufferNoun()
      } else {
        extractCompoundNoun()
      }
    }

    analyze(sentence) foreach {
      case line if line != "EOS" =>
        val tokens: Array[String] = line.split(" ")
        val pos: String = tokens(5) match {
          case "*" =>
            tokens(3)
          case otherwise =>
            "%s-%s" format (tokens(3), otherwise)
        }
        compoundNoun(tokens, StringOption(pos))
      case _ =>
    }

    extractCompoundNoun()

    terms.result
  }

  override protected def extractWords(sentence: StringOption, needsContentWords: Boolean): Seq[String] = {
    Nil
  }
}
