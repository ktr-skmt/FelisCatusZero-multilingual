package us.feliscat.ir.web.bing

import us.feliscat.ir.web
import us.feliscat.text.StringOption

/**
 * <pre>
 * Created on 6/2/15.
 * </pre>
 * @param keyword keyword
 * @author K.Sakamoto
 */
class BingKeywordQuery(override val keyword: StringOption) extends web.WebKeywordQuery(keyword) with BingQuery