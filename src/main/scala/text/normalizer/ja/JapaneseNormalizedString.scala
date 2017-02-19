package text.normalizer.ja

import java.text.{Normalizer => JavaNormalizer}

import text.{StringNone, StringOption, StringSome}

/**
  * @author K.Sakamoto
  *         Created on 15/10/28
  */
object JapaneseNormalizedString {
  def apply(str: StringOption): JapaneseNormalizedString = {
    new JapaneseNormalizedString(str)
  }
}

/**
  * @author K.Sakamoto
  * @param str string
  */
class JapaneseNormalizedString(str: StringOption) extends AnyRef {
  private val normalizedString: StringOption = normalize

  private def normalize: StringOption = {
    JapaneseNormalizer.normalize(str)
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
    case that: JapaneseNormalizedString =>
      this.toString == that.toString
    case _ => false
  }

  def concat(nStr: JapaneseNormalizedString): JapaneseNormalizedString = {
    new JapaneseNormalizedString(normalizedString.map(_.concat(nStr.toString)))
  }

  def split(regex: String): Array[JapaneseNormalizedString] = {
    normalizedString match {
      case StringSome(nStr) =>
        nStr.split(regex) map {
          token: String =>
            new JapaneseNormalizedString(StringOption(token))
        }
      case StringNone =>
        Array.empty[JapaneseNormalizedString]
    }
  }

  def split(separator: Char): Array[JapaneseNormalizedString] = {
    JapaneseNormalizer.normalize(StringOption(separator.toString)) match {
      case StringSome(nSeparator) =>
        normalizedString match {
          case StringSome(nStr) =>
            nStr.split(nSeparator.head) map {
              token: String =>
                new JapaneseNormalizedString(StringOption(token))
            }
          case StringNone =>
            Array.empty[JapaneseNormalizedString]
        }
      case StringNone =>
        Array.empty[JapaneseNormalizedString]
    }
  }

  def trim: JapaneseNormalizedString = {
    normalizedString.map(_.trim)
    this
  }
}

/**
  * @author K.Sakamoto
  */
object JapaneseNormalizer {
  def normalize(str: StringOption): StringOption = {
    def normalizeCharacters: StringOption = {
      JapaneseCharacterNormalizerBeforeUnicodeNormalization.normalize(str) map {
        s: String =>
          JapaneseCharacterNormalizerAfterUnicodeNormalization.normalize(
            StringOption(
              JavaNormalizer.normalize(s, JavaNormalizer.Form.NFKC)
            )
          ).getOrElse(null)
      }
    }

    if (str.isEmpty) {
      return StringNone
    }
    JapaneseWordExpressionNormalizer.normalize(normalizeCharacters)
  }
}

/**
  * @author K.Sakamoto
  */
class NormalizedStringBuilder {
  val builder = new StringBuilder()

  def append(nStr: JapaneseNormalizedString): NormalizedStringBuilder = {
    builder.append(nStr.toString)
    this
  }

  def result: JapaneseNormalizedString = {
    new JapaneseNormalizedString(StringOption(builder.toString()))
  }

  def clear(): Unit = {
    builder.clear
  }
}

/**
  * @author K.Sakamoto
  */
class JapaneseNormalizedStringBuffer {
  val buffer = new StringBuffer()

  def append(nStr: JapaneseNormalizedString): JapaneseNormalizedStringBuffer = {
    buffer.append(nStr.toString)
    this
  }

  def result: JapaneseNormalizedString = {
    new JapaneseNormalizedString(StringOption(buffer.toString))
  }

  def clear(): Unit = {
    buffer.setLength(0)
  }
}