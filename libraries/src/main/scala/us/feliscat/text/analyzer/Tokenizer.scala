package us.feliscat.text.analyzer

import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.util.LibrariesConfig
import us.feliscat.util.primitive.StringUtils

/**
  * @author K.Sakamoto
  *         Created on 2016/05/23
  */
object Tokenizer {
  type Tokens = Seq[String]

  def tokenize(textOpt: StringOption): Tokens = {
    textOpt match {
      case StringSome(text) =>
        LibrariesConfig.tokenizer match {
          case that if that equalsIgnoreCase "CharacterNGram" =>
            characterNGram(text)
          case _ =>
            Nil
        }
      case StringNone =>
        Nil
    }
  }

  private def characterNGram(text: String): Tokens = {
    val codePoints: Seq[Int] = text.toCodePointArray.toSeq
    codePoints.sliding(LibrariesConfig.nGram).toSeq map {
      codePoint =>
        val array: Array[Int] = codePoint.toArray
        new String(array, 0, array.length)
    }
  }
}