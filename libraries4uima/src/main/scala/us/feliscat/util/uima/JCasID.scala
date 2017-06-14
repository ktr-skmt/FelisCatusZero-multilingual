package us.feliscat.util.uima

/**
  * @author K. Sakamoto
  *         Created on 2017/06/01
  */
case class JCasID(id: String) {
  override def equals(obj: scala.Any): Boolean = {
    this.id.equals(obj.asInstanceOf[JCasID].id)
  }
}