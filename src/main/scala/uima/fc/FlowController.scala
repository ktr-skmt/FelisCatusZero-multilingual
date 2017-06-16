package uima.fc

import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData
import org.apache.uima.analysis_engine.{AnalysisEngineProcessException, TypeOrFeature}
import org.apache.uima.cas.CAS
import org.apache.uima.flow._
import org.apache.uima.resource.metadata.Capability
import org.apache.uima.util.Level

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._
import scala.util.control.Breaks

/**
  * <pre>
  * Created on 2016/12/18.
  * </pre>
  *
  * @author K.Sakamoto
  */
object FlowController {
  private val analysisEngineList = ArrayBuffer.empty[String]
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
}

class FlowController extends CasFlowController_ImplBase {
  @throws[AnalysisEngineProcessException]
  override def computeFlow(aCAS: CAS): Flow = {
    new AnalyzerFlow()
  }

  class AnalyzerFlow extends CasFlow_ImplBase {
    private val mAlreadyCalled = mutable.HashSet.empty[String]

    private var step: Int = 0

    @throws[AnalysisEngineProcessException]
    override def next(): Step = {
      if (step < 0 || FlowController.analysisEngineList.length <= step) {
        getContext.getLogger.log(Level.FINEST, "Flow Complete.")
        return new FinalStep()
      }
      print(
        s""">> Flow Controller Processing
           |${step + 1}. ${FlowController.analysisEngineList(step)}
           |""".stripMargin)
      val aCAS: CAS = getCas
      val aeIterator: Iterator[java.util.Map.Entry[String, AnalysisEngineMetaData]] =
        getContext.getAnalysisEngineMetaDataMap.entrySet.iterator.asScala.toSeq.sortBy[Int] {
          entry: java.util.Map.Entry[String, AnalysisEngineMetaData] =>
            FlowController.analysisEngineList.indexOf(entry.getKey)
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
      getContext.getLogger.log(Level.FINEST, "Flow Complete.")
      new FinalStep()
    }

    private def inputsSatisfied(aeKey: String, aInputs: Array[TypeOrFeature], aCAS: CAS): Boolean = {
      if (step < 0 || FlowController.analysisEngineList.length <= step) {
        return false
      }
      if (aeKey == FlowController.analysisEngineList(step)) {
        return true
      }
      false
    }
  }
}
