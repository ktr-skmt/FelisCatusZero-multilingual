package us.feliscat.text.normalizer.en

import us.feliscat.m17n.EnglishLocale
import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.MultiLingualDictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/02/19
  */
object EnglishWordExpressionNormalizer
  extends MultiLingualDictionaryBasedNormalizer(StringOption("word_expression_dic.yml")) with EnglishLocale {

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