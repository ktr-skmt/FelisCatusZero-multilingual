package uima.modules.ir.fulltext.indri

import java.io.{BufferedReader, File}
import java.nio.charset.{CodingErrorAction, StandardCharsets}
import java.nio.file.Files

import us.feliscat.m17n.MultiLingual
import org.apache.uima.jcas.JCas
import us.feliscat.ir.fulltext.indri.{IndriResult, Retrieval}
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.types.{BoWQuery, Document, Keyword, Score}
import us.feliscat.util.process._
import us.feliscat.util.uima.seq2fs.SeqUtils
import us.feliscat.util.uima.{FeatureStructure, JCasUtils}
import util.Config

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.sys.process.Process

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualRetrievalByBoW extends Retrieval with MultiLingual {
  private var baseline: String = "-baseline=okapi,k1:1.2,b:0.75,k3:7"
  private lazy val docnoTextMap: Map[String, String] = initialize

  protected val trecTextFormatData: Seq[String]

  private def initialize: Map[String, String] = {
    val buffer = ListBuffer.empty[String]
    trecTextFormatData foreach {
      dir: String =>
        new File(dir).listFiles foreach {
          case file: File if file.canRead && file.isFile && file.getName.endsWith(".xml") =>
            val reader: BufferedReader = Files.newBufferedReader(file.toPath, StandardCharsets.UTF_8)
            val iterator: java.util.Iterator[String] = reader.lines.iterator
            while (iterator.hasNext) {
              val line: String = iterator.next
              buffer += line
            }
            reader.close()
          case _ =>
          // Do nothing
        }
    }
    val map = mutable.Map.empty[String, String]
    val iterator: Iterator[IndriResult] = toIndriResultMap(
      buffer.result.iterator,
      StringNone,
      Nil,
      mutable.Map.empty[String, IndriResult]).valuesIterator
    while (iterator.hasNext) {
      val result: IndriResult = iterator.next
      result.docno match {
        case StringSome(docno) if !map.contains(docno) =>
          val textOpt: StringOption = result.text
          if (textOpt.nonEmpty) {
            map(docno) = textOpt.get
          }
        case _ =>
        // Do nothing
      }
    }
    map.toMap
  }

  def retrieve(aJCas: JCas,
               query: BoWQuery,
               mIndriScoreIndex: Int,
               firstDocumentId: Long): Long = {
    var mDocumentId: Long = firstDocumentId
    JCasUtils.setAJCasOpt(Option(aJCas))

    query.getAlgorithm match {
      case "TFIDF" =>
        baseline = s"""-baseline=tfidf,k1${Config.indriTfidfK1},b:${Config.indriTfidfB}"""
      case "BM25" =>
        baseline = s"""-baseline=okapi,k1:${Config.indriBm25K1},b:${Config.indriBm25B},k3:${Config.indriBm25K3}"""
      case _ =>
      // Do nothing
    }

    val knowledgeSourceList: Seq[String] = selectKnowledgeSource(false)

    val keyword: Keyword = query.getIndriQuery
    println(keyword.getText)
    def retrieve: Iterator[IndriResult] = {
      toIndriResultMap(
        Process(command(Seq[String](keyword.getText), knowledgeSourceList)).
          lineStream(
            StandardCharsets.UTF_8,
            CodingErrorAction.IGNORE,
            CodingErrorAction.IGNORE,
            StringNone,
            Config.indriRunQueryTimeout.minute
          ),
        StringOption(keyword.getText), Nil, mutable.Map.empty[String, IndriResult]).valuesIterator
    }

    //docno, document
    val documentMap = mutable.Map.empty[String, Document]

    retrieve foreach {
      case result if result.text.nonEmpty && result.docno.nonEmpty && result.title.nonEmpty =>
        val docno: String = result.docno.get
        val title: String = result.title.get
        val score: Double = result.score
        val textOpt: StringOption = docno2TextOpt(docno)
        if (!documentMap.contains(docno) && textOpt.nonEmpty) {
          val document = FeatureStructure.create[Document]
          document.setText(textOpt.get)
          document.setDocno(docno)
          document.setTitle(title)
          val scoreArray = FeatureStructure.createArray(Config.numOfScores)
          for (i <- 0 until scoreArray.size()) {
            val scoreType = FeatureStructure.create[Score]
            scoreArray.set(i, scoreType)
          }
          val scoreType: Score = scoreArray.get(mIndriScoreIndex).asInstanceOf[Score]
          scoreType.setScore(score)
          scoreType.setScorer("WordLevel" concat query.getAlgorithm)
          document.setScoreList(scoreArray)
          mDocumentId += 1
          document.setId(mDocumentId.toString)
          documentMap(docno) = document
        }
      case _ =>
      // Do nothing
    }
    val documentList: Seq[Document] = documentMap.values.toSeq
    keyword.setDocumentSet(documentList.toFSArray)
    query.setAlreadyFinishedRetrieving(true)
    mDocumentId
  }

  private def docno2TextOpt(docno: String): StringOption = {
    if (docnoTextMap.contains(docno)) {
      StringOption(docnoTextMap(docno))
    } else {
      StringNone
    }
  }

  override def command(queryList: Seq[String], knowledgeSourceList: Seq[String]): Seq[String] = {
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
      s"-memory=${Config.indriMemory}" ::
      "-printQuery=true" ::
      baseline ::
      s"-count=${Config.indriCount}" :: Nil
    ) ++ queries ++ indices
  }
}
