package ir.web

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import ir.query.KeywordQuery
import text.{StringNone, StringOption}

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
