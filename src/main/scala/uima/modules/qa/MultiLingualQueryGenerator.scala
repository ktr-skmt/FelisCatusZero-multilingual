package uima.modules.qa

import us.feliscat.m17n.MultiLingual
import org.apache.uima.jcas.JCas
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.types._
import us.feliscat.util.uima.fsList._
import us.feliscat.util.uima.seq2fs.SeqUtils

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQueryGenerator extends MultiLingual {
  private val keywordQueryCache = mutable.Map.empty[String, KeywordQuery]
  private val bowQueryCache = mutable.Map.empty[String, BoWQuery]
  protected type Quotation = (String, String)
  protected def expand(keywordOpt: StringOption): Seq[(String, Boolean)]
  def generate(aJCas: JCas, question: Question): Seq[Query] = {
    val queryBuffer = ListBuffer.empty[Query]

    val keywordList: Seq[Keyword] = question.getKeywordSet.toSeq.asInstanceOf[Seq[Keyword]]

    //mandatory keywords
    keywordList foreach {
      keyword: Keyword =>
        StringOption(keyword.getText) match {
          case StringSome(keywordText) =>
            if (keywordQueryCache.contains(keywordText)) {
              queryBuffer += keywordQueryCache(keywordText)
            } else {
              val query = new KeywordQuery(aJCas)
              query.addToIndexes()
              val expansionBuffer = ListBuffer.empty[KeywordExpansion]
              expand(StringOption(keywordText)) foreach {
                case (expansionText, isMandatory) =>
                  val expansion = new KeywordExpansion(aJCas)
                  expansion.addToIndexes()
                  expansion.setText(expansionText)
                  expansion.setIsMandatory(isMandatory)
                  expansionBuffer += expansion
                case _ =>
                // Do nothing
              }
              keyword.setExpansionSet(expansionBuffer.result.toFSList)
              query.setKeyword(keyword)
              query.setAlreadyFinishedRetrieving(false)
              query.setAlreadyFinishedCorrecting(false)
              //query.setKnowledgeSourceList(KnowledgeSourceSelector.select(true).toStringList)
              keywordQueryCache(keywordText) = query
              queryBuffer += query
            }
          case StringNone =>
          // Do nothing
        }
    }

    //optional keywords
    if (keywordList.isEmpty) {
      val contentWordList: Seq[String] = question.getDocument.getContentWordList.toSeq.sorted
      val indriQuery: String = contentWordList.mkString("", " ", "")
      if (bowQueryCache.contains(indriQuery)) {
        queryBuffer += bowQueryCache(indriQuery)
      } else {
        val query = new BoWQuery(aJCas)
        query.addToIndexes()
        val keyword = new Keyword(aJCas)
        keyword.addToIndexes()
        keyword.setText(indriQuery)
        query.setIndriQuery(keyword)
        query.setAlgorithm("BM25")
        query.setAlreadyFinishedRetrieving(false)
        query.setAlreadyFinishedCorrecting(false)
        //query.setKnowledgeSourceList(KnowledgeSourceSelector.select(false).toStringList)
        bowQueryCache(indriQuery) = query
        queryBuffer += query
      }
    }

    queryBuffer.result
  }
}
