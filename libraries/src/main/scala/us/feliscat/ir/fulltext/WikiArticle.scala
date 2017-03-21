package us.feliscat.ir.fulltext

import us.feliscat.converter.WikiArticleConverter
import org.jsoup.nodes.Document
import us.feliscat.text.StringOption

/**
 * <pre>
 * Created on 6/3/15.
 * </pre>
 * @param title title
 * @param article article
 * @author K.Sakamoto
 */
class WikiArticle(val title: StringOption, val article: StringOption) {
  def html: Option[Document] = {
      WikiArticleConverter.toHtml(article)
  }

  def text: StringOption = {
    WikiArticleConverter.toText(article)
  }
}
