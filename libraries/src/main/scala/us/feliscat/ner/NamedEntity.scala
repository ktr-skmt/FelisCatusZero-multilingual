package us.feliscat.ner

import us.feliscat.text.StringOption
import us.feliscat.time.TimeTmp

/**
  * @author K.Sakamoto
  *         Created on 2015/11/26
  */
object NamedEntity {
  def apply(textOpt: StringOption,
            time: TimeTmp,
            indexOffset: Range,
            metaInfoOpt: StringOption,
            recognizerOpt: StringOption,
            synonyms: Seq[String]): NamedEntity = {
    new NamedEntity(textOpt, time, indexOffset, metaInfoOpt, recognizerOpt, synonyms)
  }
}

/**
  * @author K.Sakamoto
  * @param textOpt us.feliscat.text
  * @param time us.feliscat.time
  * @param indexOffset index and offset
  * @param metaInfoOpt meta info
  * @param recognizerOpt recognizer
  * @param synonyms synonyms
  */
class NamedEntity(val textOpt: StringOption,
                  val time: TimeTmp,
                  val indexOffset: Range,
                  val metaInfoOpt: StringOption,
                  val recognizerOpt: StringOption,
                  val synonyms: Seq[String])
