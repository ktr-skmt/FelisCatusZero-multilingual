package us.feliscat.text.normalizer.en

import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.text.analyzer.CoreNLP4English

/**
  * @author K. Sakamoto
  *         Created on 2017/07/12
  */
object EnglishStopWords extends English {
  def remove(sentenceOpt: StringOption): StringOption = {
    sentenceOpt map {
      sentence: String =>
        var res: String = sentence
        CoreNLP4English.stopWords foreach {
          stopWord: String =>
            val lowerCaseWord: String = stopWord.toLowerCase(locale)
            val upperCaseWord: String = stopWord.toUpperCase(locale)
            val titleCaseWord: String = stopWord.head.toTitleCase.toString.concat(stopWord.tail)
            val regex: String = s"""\\s?(?:$lowerCaseWord|$upperCaseWord|$titleCaseWord)\\s?"""
            res = res.
              replaceAll(
                regex,
                " ")
        }
        res
    }
  }
}
