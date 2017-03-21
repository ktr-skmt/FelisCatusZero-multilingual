package us.feliscat.converter.en

import us.feliscat.converter.MultiLingualNgramSegmentator
import us.feliscat.m17n.English

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishNgramSegmentator(nGram: Int) extends MultiLingualNgramSegmentator(nGram) with English
