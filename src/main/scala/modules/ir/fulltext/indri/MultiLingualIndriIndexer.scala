package modules.ir.fulltext.indri

import java.nio.file.{Path, Paths}

import us.feliscat.ir.fulltext.indri.{IndriIndexer, MultiLingualTrecTextFileFormatReviser}
import us.feliscat.m17n.MultiLingual

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualIndriIndexer extends MultiLingual {
  protected val indices:       Array[String]
  protected val segmentations: Array[String]
  protected val resources:     Array[String]

  protected val reviser: MultiLingualTrecTextFileFormatReviser

  def run(): Unit = {
    print(
      s"""Resources (length = ${resources.length}):
         |${resources.mkString("\n")}
         |Segmentations (length = ${segmentations.length}):
         |${segmentations.mkString("\n")}
         |Indices (length = ${indices.length}):
         |${indices.mkString("\n")}
         |""".stripMargin)
    if (segmentations.length == indices.length &&
        resources.length == indices.length) {
      for (i <- indices.indices) {
        val resource:     Path = Paths.get(resources(i))
        val segmentation: Path = Paths.get(segmentations(i))
        val indexPath:    Path = Paths.get(indices(i))

        println(">> Indri Segmentation Processing")
        reviser.reviseInDirectory(resource, segmentation)
        println(">> Indri Segmentation Finishing")

        println(">> IndriBuildIndex Processing")
        val indexer = new IndriIndexer(segmentation, indexPath)
        indexer.index()
        println(">> IndriBuildIndex Finishing")

        println()
      }
    }
  }

  def main(args: Array[String]): Unit = {
    run()
  }
}