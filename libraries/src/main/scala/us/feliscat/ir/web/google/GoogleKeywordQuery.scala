package us.feliscat.ir.web.google

import us.feliscat.ir.web
import us.feliscat.text.StringOption

/**
 * <pre>
 * Created on 6/2/15.
 * </pre>
 * @author K.Sakamoto
 */
class GoogleKeywordQuery(override val keyword: StringOption) extends web.WebKeywordQuery(keyword) with GoogleQuery
