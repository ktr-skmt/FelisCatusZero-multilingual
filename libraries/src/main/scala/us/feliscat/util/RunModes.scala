package us.feliscat.util

/**
  * @author K. Sakamoto
  *         Created on 2017/08/09
  */
object RunModes {
  sealed trait RunMode

  final object Development   extends RunMode
  final object ProcessDetail extends RunMode
  final object Test          extends RunMode
  final object Pilot         extends RunMode
  final object Production    extends RunMode
}
