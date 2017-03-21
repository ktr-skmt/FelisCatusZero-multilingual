package us.feliscat.ir.web

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import us.feliscat.ir.query.KeywordQuery
import us.feliscat.text.{StringNone, StringOption}

/**
 * <pre>
 * Created on 6/2/15.
 * </pre>
 * @author K.Sakamoto
 */
class WebKeywordQuery(override val keyword: StringOption) extends KeywordQuery(keyword) with WebQuery {
  override val query: StringOption = try {
    keyword map {
      k: String =>
        URLEncoder.encode(
          k.toString,
          StandardCharsets.UTF_8.name)
    }
  } catch {
    case e: UnsupportedEncodingException =>
      e.printStackTrace()
      StringNone
  }
}
