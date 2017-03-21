package us.feliscat.ir.web

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import us.feliscat.ir.query.KeywordsQuery
import us.feliscat.text.{StringNone, StringOption}

/**
 * <pre>
 * Created on 6/1/15.
 * </pre>
 * @param keywords keywords
 * @author K.Sakamoto
 */
class WebKeywordsQuery(override val keywords: Seq[String]) extends KeywordsQuery(keywords) with WebQuery {
  override val query: StringOption = try {
    StringOption(URLEncoder.encode(generate, StandardCharsets.UTF_8.name))
  } catch {
    case e: UnsupportedEncodingException =>
      e.printStackTrace()
      StringNone
  }

  protected val delimiter: Char = ' '

  private def generate: String = {
    val builder = new StringBuilder(keywords.size * 2 - 1)
    keywords foreach {
      query: String =>
        builder.append(delimiter).
                append(query)
    }
    builder.deleteCharAt(0).result
  }
}
