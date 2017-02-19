package ir.fulltext.indri

import m17n.MultiLingual
import text.StringOption
import util.Config

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/01/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait Retrieval extends MultiLingual {
  protected def selectKnowledgeSource(isKeywordQuery: Boolean): Seq[String]
  protected def command(queryList: Seq[String], knowledgeSourceList: Seq[String]): Seq[String] = {
    val indices: Seq[String] = knowledgeSourceList map {
      knowledgeSource: String =>
        "-index=" concat knowledgeSource
    }
    val queries: Seq[String] = queryList map {
      query: String =>
        "-query=" concat query
    }
    Seq[String](
      "IndriRunQuery",
      "-printDocuments=true",
      s"-memory=${Config.indriMemory}",
      "-printQuery=true",
      s"-count=${Config.indriCount}") ++ queries ++ indices
  }
  protected def toIndriResultMap(lines: Iterator[String],
                                 keywordOriginalTextOpt: StringOption,
                                 expansionOnlyList: Seq[String],
                                 indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult]
}
