package ner

import m17n.MultiLingual
import text.StringOption
import time.TimeTmp

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualNamedEntityRecognizerForWorldHistory extends NamedEntityRecognizer with MultiLingual {
  //This is not used
  override protected val recognizerName: String = null

  //This is not used
  override protected def initialize: NEList = Nil

  //This is not used
  override protected val entityList: NEList = initialize

  //This is not used
  override protected def extract(sentence: StringOption): Seq[TimeTmp] = Seq.empty[TimeTmp]
}
