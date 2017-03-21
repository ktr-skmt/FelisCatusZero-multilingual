package us.feliscat.text.normalizer.en

import java.text.{Normalizer => JavaNormalizer}

import us.feliscat.text.{StringNone, StringOption, StringSome}

/**
  * @author K.Sakamoto
  *         Created on 15/10/28
  */
object EnglishNormalizedString {
  def apply(str: StringOption): EnglishNormalizedString = {
    new EnglishNormalizedString(str)
  }
}

/**
  * @author K.Sakamoto
  * @param str string
  */
class EnglishNormalizedString(str: StringOption) extends AnyRef {
  private val normalizedString: StringOption = normalize

  private def normalize: StringOption = {
    EnglishNormalizer.normalize(str)
  }

  override def toString: String = {
    normalizedString.getOrElse("")
  }

  def toStringOption: StringOption = {
    normalizedString
  }

  override def hashCode: Int = {
    this.toString.toInt
  }

  override def equals(other: Any): Boolean = other match {
    case that: EnglishNormalizedString =>
      this.toString == that.toString
    case _ => false
  }

  def concat(nStr: EnglishNormalizedString): EnglishNormalizedString = {
    new EnglishNormalizedString(normalizedString.map(_.concat(nStr.toString)))
  }

  def split(regex: String): Array[EnglishNormalizedString] = {
    normalizedString match {
      case StringSome(nStr) =>
        nStr.split(regex) map {
          token: String =>
            new EnglishNormalizedString(StringOption(token))
        }
      case StringNone =>
        Array.empty[EnglishNormalizedString]
    }
  }

  def split(separator: Char): Array[EnglishNormalizedString] = {
    EnglishNormalizer.normalize(StringOption(separator.toString)) match {
      case StringSome(nSeparator) =>
        normalizedString match {
          case StringSome(nStr) =>
            nStr.split(nSeparator.head) map {
              token: String =>
                new EnglishNormalizedString(StringOption(token))
            }
          case StringNone =>
            Array.empty[EnglishNormalizedString]
        }
      case StringNone =>
        Array.empty[EnglishNormalizedString]
    }
  }

  def trim: EnglishNormalizedString = {
    normalizedString.map(_.trim)
    this
  }
}

/**
  * @author K.Sakamoto
  */
object EnglishNormalizer {
  def normalize(str: StringOption): StringOption = {
    def normalizeCharacters: StringOption = {
      EnglishCharacterNormalizerBeforeUnicodeNormalization.normalize(str) map {
        s: String =>
          EnglishCharacterNormalizerAfterUnicodeNormalization.normalize(
            StringOption(
              JavaNormalizer.normalize(s, JavaNormalizer.Form.NFKC)
            )
          ).getOrElse(null)
      }
    }

    if (str.isEmpty) {
      return StringNone
    }
    EnglishWordExpressionNormalizer.normalize(normalizeCharacters)
  }
}

/**
  * @author K.Sakamoto
  */
class EnglishNormalizedStringBuilder {
  val builder = new StringBuilder()

  def append(nStr: EnglishNormalizedString): EnglishNormalizedStringBuilder = {
    builder.append(nStr.toString)
    this
  }

  def result: EnglishNormalizedString = {
    new EnglishNormalizedString(StringOption(builder.toString()))
  }

  def clear(): Unit = {
    builder.clear
  }
}

/**
  * @author K.Sakamoto
  */
class EnglishNormalizedStringBuffer {
  val buffer = new StringBuffer()

  def append(nStr: EnglishNormalizedString): EnglishNormalizedStringBuffer = {
    buffer.append(nStr.toString)
    this
  }

  def result: EnglishNormalizedString = {
    new EnglishNormalizedString(StringOption(buffer.toString))
  }

  def clear(): Unit = {
    buffer.setLength(0)
  }
}