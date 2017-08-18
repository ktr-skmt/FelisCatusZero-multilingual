/*
package uima.fc

import uima.cpe.IntermediatePoint

/**
  * @author K. Sakamoto
  *         Created on 2017/06/24
  */
abstract class AEFlowController extends AAEFlowController {
  protected val intermediatePoint: IntermediatePoint

  override protected def processingLabel(step: Int): String = {
    s""">> ${intermediatePoint.name} Flow Controller Processing
       |${intermediatePoint.id}.${step + 1} ${analysisEngineList(step)}
       |""".stripMargin
  }

  override def printAnalysisEngines(): Unit = {
    println(s"${intermediatePoint.name} Flow Controller Analysis Engine List:")
    if (analysisEngineList.isEmpty) {
      println("(empty)")
      return
    }
    var counter: Int = 0
    analysisEngineList foreach {
      analysisEngine: String =>
        counter += 1
        println(s"${intermediatePoint.id}.$counter $analysisEngine")
    }
  }
}
*/