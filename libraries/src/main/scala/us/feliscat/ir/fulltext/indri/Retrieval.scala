package us.feliscat.ir.fulltext.indri

import us.feliscat.m17n.MultiLingual
import us.feliscat.text.StringOption
import us.feliscat.util.LibrariesConfig

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
    (
      "IndriRunQuery" ::
      "-printDocuments=true" ::
      s"-memory=${LibrariesConfig.indriMemory}" ::
      "-printQuery=true" ::
      s"-count=${LibrariesConfig.indriCount}" :: Nil
    ) ++ queries ++ indices
  }
  protected def toIndriResultMap(lines: Iterator[String],
                                 keywordOriginalTextOpt: StringOption,
                                 expansionOnlyList: Seq[String],
                                 indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult]
}
