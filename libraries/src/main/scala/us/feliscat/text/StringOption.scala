package us.feliscat.text

/**
  * @author K.Sakamoto
  *         Created on 15/10/28
  */
object StringOption {
  def apply(value: String): StringOption = {
    if (value == null || value == "") {
      StringNone
    } else {
      StringSome(value)
    }
  }

  def empty: StringOption = StringNone
}

/**
  * @author K.Sakamoto
  */
sealed abstract class StringOption {
  def getOrElse(value: String): String = {
    this match {
      case StringSome(s) => s
      case StringNone => value
    }
  }

  def codePointCount: Int = {
    this match {
      case StringSome(s) =>
        import us.feliscat.util.primitive.StringUtils
        s.codePointNumber
      case StringNone => 0
    }
  }

  def isEmpty: Boolean = {
    this == StringNone
  }

  def nonEmpty: Boolean = {
    !isEmpty
  }

  def get: String = {
    throw new NoSuchElementException("StringNone.get")
  }

  def map(f: String => String): StringOption =
    if (isEmpty) {
      StringNone
    } else {
      StringSome(f(get))
    }

  def foreach(f: String => Unit): Unit = if (nonEmpty) f(get)
}

/**
  * @author K.Sakamoto
  * @param value value
  */
final case class StringSome(value: String) extends StringOption {
  override def get: String = {
    value
  }
}

/**
  * @author K.Sakamoto
  */
sealed abstract class StringNoneOption extends StringOption

/**
  * @author K.Sakamoto
  */
object StringNone extends StringNoneOption

