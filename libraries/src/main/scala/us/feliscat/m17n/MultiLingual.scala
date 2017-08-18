package us.feliscat.m17n

import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingual extends MultiLingualLocale {
  protected def normalize(textOpt: StringOption): StringOption
}
