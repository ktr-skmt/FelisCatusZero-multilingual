package us.feliscat.ir.query

import us.feliscat.ir.Query
import us.feliscat.text.StringOption

/**
 * <pre>
 * Created on 6/1/15.
 * </pre>
 * @param keyword keyword
 * @author K.Sakamoto
 */
abstract class KeywordQuery(val keyword: StringOption) extends Query {
  override val query: StringOption
}
