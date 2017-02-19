package ir.web.bing

import ir.web
import text.StringOption

/**
 * <pre>
 * Created on 6/2/15.
 * </pre>
 * @param keyword keyword
 * @author K.Sakamoto
 */
class BingKeywordQuery(override val keyword: StringOption) extends web.WebKeywordQuery(keyword) with BingQuery