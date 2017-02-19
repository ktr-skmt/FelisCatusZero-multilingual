package ir.fulltext.indri

import java.nio.file.{Path, Paths}

import m17n.MultiLingual

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
    if (segmentations.length == indices.length &&
        resources.length == indices.length) {
      for (i <- indices.indices) {
        val resource:     Path = Paths.get(resources(i))
        val segmentation: Path = Paths.get(segmentations(i))
        val indexPath:    Path = Paths.get(indices(i))

        reviser.reviseInDirectory(resource, segmentation)
        new IndriIndex(segmentation, indexPath).index()
        println()
      }
    }
  }

  def main(args: Array[String]): Unit = {
    run()
  }
}
