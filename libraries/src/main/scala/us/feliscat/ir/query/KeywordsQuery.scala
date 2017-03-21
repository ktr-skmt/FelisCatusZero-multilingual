package us.feliscat.ir.query

import us.feliscat.ir.Query
import us.feliscat.text.StringOption

/**
 * <pre>
 * Created on 6/1/15.
 * </pre>
 * @param keywords keywords
 * @author K.Sakamoto
 */
abstract class KeywordsQuery(val keywords: Seq[String]) extends Query {
  override val query: StringOption
}
