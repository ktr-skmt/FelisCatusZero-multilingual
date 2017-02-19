package m17n

import text.StringOption

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingual {
  protected val localeId: String

  protected def normalize(textOpt: StringOption): StringOption
}
