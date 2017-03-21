package us.feliscat.ir.web.bing

import us.feliscat.ir.web.{SearchedPageListBuilder, SearchedPageListBuilderImpl}

/**
 * <pre>
 * Created on 6/1/15.
 * </pre>
 * @author K.Sakamoto
 */
object BingSearchedPageListBuilder extends SearchedPageListBuilderImpl {
  override def apply(): SearchedPageListBuilder = {
    new BingSearchedPageListBuilder(16)
  }

  override def apply(capacity: Int): SearchedPageListBuilder = {
    new BingSearchedPageListBuilder(capacity)
  }

  private class BingSearchedPageListBuilder(capacity: Int) extends SearchedPageListBuilder(capacity)
}
