package us.feliscat.ir.web.bing

import java.net.URL

import us.feliscat.ir.Query
import us.feliscat.ir.web

/**
 * <pre>
 * Created on 6/1/15.
 * </pre>
 * @param searchedPages searched pages
 * @param query query
 * @param startIndex starting index
 * @param currentUrl current URL
 * @param nextUrl next URL
 * @author K.Sakamoto
 */
class BingSearchedPageList(override val searchedPages: Array[web.SearchedPage],
                           override val query: Query,
                           override val startIndex: Int,
                           override val currentUrl: URL,
                           override val nextUrl: URL) extends web.SearchedPageList(searchedPages, query, startIndex, currentUrl, nextUrl)