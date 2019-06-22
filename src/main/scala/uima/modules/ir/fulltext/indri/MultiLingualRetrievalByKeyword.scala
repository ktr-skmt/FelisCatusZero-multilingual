package uima.modules.ir.fulltext.indri

import java.nio.charset.{CodingErrorAction, StandardCharsets}

import us.feliscat.m17n.MultiLingual
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import us.feliscat.ir.fulltext.indri.{IndriResult, Retrieval}
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.types._
import us.feliscat.util.process._
import us.feliscat.util.uima.fsList.FSListUtils
import us.feliscat.util.uima.{JCasID, JCasUtils}
import us.feliscat.util.uima.seq2fs.SeqUtils
import util.Config

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.sys.process.Process
import scala.util.{Failure, Success, Try}

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualRetrievalByKeyword extends Retrieval with MultiLingual {
  def retrieve(aJCas: JCas,
               query: KeywordQuery,
               keywordCorrectionMap: mutable.Map[String, Seq[String]],
               mIndriScoreIndex: Int,
               firstDocumentId: Long)(implicit id: JCasID): Long = {
    var mDocumentId: Long = firstDocumentId
    JCasUtils.setAJCas(aJCas)

    val knowledgeSourceList: Seq[String] = selectKnowledgeSource(true)

    val keyword: Keyword = query.getKeyword
    val mandatoryExpansionBuffer = ListBuffer.empty[String]
    val optionalExpansionBuffer = ListBuffer.empty[String]
    keyword.getExpansionSet.toSeq.asInstanceOf[Seq[KeywordExpansion]] foreach {
      expansion: KeywordExpansion =>
        if (expansion.getIsMandatory) {
          mandatoryExpansionBuffer += expansion.getText
        } else {
          optionalExpansionBuffer += expansion.getText
        }
    }
    val keywordOriginalText: String = keyword.getText
    val mandatoryExpansionList: Seq[String] = mandatoryExpansionBuffer.result.
      distinct.
      sortWith((a, b) => a.length > b.length)
    val optionalExpansionList: Seq[String] = optionalExpansionBuffer.result.
      distinct.
      sortWith((a, b) => a.length > b.length)
    val expansionOnlyList: Seq[String] = (mandatoryExpansionList.diff(keywordOriginalText) ++ optionalExpansionList).
      filterNot(expansion => StringOption(expansion).isEmpty).
      distinct.
      sortWith((a, b) => a.length > b.length)
    keywordCorrectionMap(keywordOriginalText) = {
      if (!(keywordCorrectionMap contains keywordOriginalText)) {
        expansionOnlyList
      } else {
        keywordCorrectionMap(keywordOriginalText) ++ expansionOnlyList
      }
    }

    def retrieve: Iterator[IndriResult] = {
      if (1 < mandatoryExpansionList.size) {
        //original + mandatory
        val resultList: Iterator[IndriResult] = retrieveInIndri(
          keywordOriginalText,
          mandatoryExpansionList,
          expansionOnlyList,
          knowledgeSourceList
        )
        if (resultList.isEmpty) {
          val listBuffer = ListBuffer.empty[String]
          listBuffer ++= optionalExpansionList
          listBuffer += keywordOriginalText
          val list: Seq[String] = listBuffer.result.
            filterNot(expansion => StringOption(expansion).isEmpty).
            distinct.
            sortWith((a, b) => a.length > b.length)
          retrieveInIndri(
            keywordOriginalText,
            list,
            expansionOnlyList,
            knowledgeSourceList
          )
        } else {
          resultList
        }
      } else {
        //original + optional
        val list: Seq[String] = (mandatoryExpansionList ++ optionalExpansionList).
          filterNot(expansion => StringOption(expansion).isEmpty).
          distinct.
          sortWith((a, b) => a.length > b.length)
        retrieveInIndri(
          keywordOriginalText,
          list,
          expansionOnlyList,
          knowledgeSourceList
        )
      }
    }

    //docno, document
    val documentMap = mutable.Map.empty[String, Document]

    retrieve foreach {
      case result if result.text.nonEmpty && result.docno.nonEmpty && result.title.nonEmpty =>
        val docno: String = result.docno.get
        val title: String = result.title.get
        val score: Double = result.score
        if (!documentMap.contains(docno)) {
          val document = new Document(aJCas)
          document.addToIndexes()
          document.setText(result.text.get)
          document.setDocno(docno)
          document.setTitle(title)
          val scoreArray = new FSArray(aJCas, Config.numOfScores)
          scoreArray.addToIndexes()
          for (i <- 0 until scoreArray.size()) {
            val scoreType = new Score(aJCas)
            scoreType.addToIndexes()
            scoreArray.set(i, scoreType)
          }
          val scoreType: Score = scoreArray.get(mIndriScoreIndex).asInstanceOf[Score]
          scoreType.setScore(score)
          scoreType.setScorer("CharacterLevelIndri")
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


  private def generateIndriQueryList(expansionSet: Seq[String]): Seq[StringOption] = {
    expansionSet map {
      k: String =>
        segment(normalize(StringOption(k))) map {
          uniGram: String =>
            s"""#2($uniGram)"""
        }
    }
  }

  protected def segment(text: StringOption): StringOption

  private def retrieveInIndri(keywordOriginalText: String,
                              expansionSet: Seq[String],
                              expansionOnlyList: Seq[String],
                              knowledgeSourceList: Seq[String]): Iterator[IndriResult] = {
    val indriResultMap = mutable.Map.empty[String, IndriResult]
    generateIndriQueryList(expansionSet) foreach {
      case StringSome(q) =>
        println(q)
        Try {
          Process(command(Seq[String](q), knowledgeSourceList)).
            lineStream(
              StandardCharsets.UTF_8,
              CodingErrorAction.IGNORE,
              CodingErrorAction.IGNORE,
              StringNone: StringOption,
              Config.indriRunQueryTimeout.minute: FiniteDuration
            )
        } match {
          case Success(result) =>
            indriResultMap ++= toIndriResultMap(
              result,
              StringOption(keywordOriginalText),
              expansionOnlyList,
              indriResultMap)
          case Failure(err: Throwable) =>
            err.printStackTrace(System.err)
        }
      case StringNone =>
      // Do nothing
    }
    indriResultMap.result.valuesIterator
  }
}
