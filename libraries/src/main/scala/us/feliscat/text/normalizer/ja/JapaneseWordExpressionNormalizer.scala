package us.feliscat.text.normalizer.ja

import us.feliscat.m17n.JapaneseLocale
import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.MultiLingualDictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/02/19
  */
object JapaneseWordExpressionNormalizer
  extends MultiLingualDictionaryBasedNormalizer(StringOption("word_expression_dic.yml")) with JapaneseLocale {

  implicit val order = new Ordering[String]{
    override def compare(x: String, y: String): Int = collator.compare(x, y)
  }

  override protected def sortNotationVariants(notationVariants: List[String]): List[String] = {
    super.sortNotationVariants(notationVariants).
      sorted.reverse.//alphabetical descending order
      sortWith((a, b) => a.length > b.length)//length descending order
  }

  override protected def sortRepresentations(representations: List[String]): List[String] = {
    super.sortRepresentations(representations).
      sorted.//alphabetical ascending order
      sortWith((a, b) => a.length < b.length)//length ascending order
  }

  override protected def replaceAll(input: String, term: String, replacement: String): String = {
    input.replaceAll(term, replacement)
  }
}