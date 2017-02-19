package ir.web.google

import ir.web
import text.StringOption

/**
 * <pre>
 * Created on 6/2/15.
 * </pre>
 * @author K.Sakamoto
 */
class GoogleKeywordQuery(override val keyword: StringOption) extends web.WebKeywordQuery(keyword) with GoogleQuery
