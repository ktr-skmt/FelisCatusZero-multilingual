package us.feliscat.text.vector

import us.feliscat.util.LibrariesConfig

/**
  * @author K.Sakamoto
  *         Created on 2016/05/22
  */
object VectorType extends Enumeration {
  val None,
  Binary,
  Frequency = Value

  def get: VectorType.Value = {
    if (LibrariesConfig.isFrequencyOtherwiseBinary) {
      Frequency
    } else {
      Binary
    }
  }
}