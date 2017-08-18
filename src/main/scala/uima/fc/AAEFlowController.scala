/*
package uima.fc

import org.apache.uima.analysis_engine.{AnalysisEngineProcessException, TypeOrFeature}
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData
import org.apache.uima.cas.CAS
import org.apache.uima.flow._
import org.apache.uima.resource.metadata.Capability
import org.apache.uima.util.Level

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks

/**
  * @author K. Sakamoto
  *         Created on 2017/06/24
  */
class AAEFlowController extends CasFlowController_ImplBase {
  protected val analysisEngineList: ArrayBuffer[String] = ArrayBuffer.empty[String]

  def setAnalysisEngine(analysisEngine: String): Unit = {
    analysisEngineList += analysisEngine
  }

  def clear(): Unit = {
    analysisEngineList.clear
  }

  def hasAnalysisEngine(analysisEngine: String): Boolean = {
    analysisEngineList.contains(analysisEngine)
  }

  def printAnalysisEngines(): Unit = {
    println("Flow Controller Analysis Engine List:")
    if (analysisEngineList.isEmpty) {
      println("(empty)")
      return
    }
    var counter: Int = 0
    analysisEngineList foreach {
      analysisEngine: String =>
        counter += 1
        println(s"$counter. $analysisEngine")
    }
  }

  def indexOf(analysisEngine: String): Int = {
    analysisEngineList.indexOf(analysisEngine)
  }

  def insert(index: Int, analysisEngine: String): Unit = {
    analysisEngineList.insert(index, analysisEngine)
  }

  def isOutOfIndex(step: Int): Boolean = {
    step < 0 || analysisEngineList.length <= step
  }

  protected def processingLabel(step: Int): String = {
    s""">> Flow Controller Processing
       |${step + 1}. ${analysisEngineList(step)}
       |""".stripMargin
  }

  protected def completingLabel(): String = {
    "Flow Complete."
  }

  @throws[AnalysisEngineProcessException]
  override def computeFlow(aCAS: CAS): Flow = {
    new AnalyzerFlow()
  }

  class AnalyzerFlow extends CasFlow_ImplBase {
    private val mAlreadyCalled = mutable.HashSet.empty[String]

    private var step: Int = 0

    @throws[AnalysisEngineProcessException]
    override def next(): Step = {
      if (isOutOfIndex(step)) {
        getContext.getLogger.log(Level.FINEST, completingLabel())
        return new FinalStep()
      }
      print(processingLabel(step))
      val aCAS: CAS = getCas
      val aeIterator: Iterator[java.util.Map.Entry[String, AnalysisEngineMetaData]] =
        getContext.getAnalysisEngineMetaDataMap.entrySet.iterator.asScala.toSeq.sortBy[Int] {
          entry: java.util.Map.Entry[String, AnalysisEngineMetaData] =>
            analysisEngineList.indexOf(entry.getKey)
        }.iterator
      while (aeIterator.hasNext) {
        val entry: java.util.Map.Entry[String, AnalysisEngineMetaData] = aeIterator.next
        val aeKey: String = entry.getKey
        if (!(mAlreadyCalled contains aeKey)) {
          val metaData: AnalysisEngineMetaData = entry.getValue
          val caps: Array[Capability] = metaData.getCapabilities
          var satisfied = true
          val loop = new Breaks()
          loop.breakable {
            for (i <- caps.indices) {
              satisfied = inputsSatisfied(aeKey, caps(i).getInputs, aCAS)
              if (satisfied) {
                loop.break
              }
            }
          }
          if (satisfied) {
            mAlreadyCalled.add(aeKey)
            //if (mLogger.isLoggable(Level.FINEST)) {
            getContext.getLogger.log(Level.FINEST, s"Next AE is: $aeKey")
            //}
            step += 1
            return new SimpleStep(aeKey)
          }
        }
      }
      getContext.getLogger.log(Level.FINEST, completingLabel())
      new FinalStep()
    }

    private def inputsSatisfied(aeKey: String, aInputs: Array[TypeOrFeature], aCAS: CAS): Boolean = {
      if (isOutOfIndex(step)) {
        return false
      }
      if (aeKey == analysisEngineList(step)) {
        return true
      }
      false
    }
  }
}
*/