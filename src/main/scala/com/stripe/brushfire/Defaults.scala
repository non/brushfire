package com.stripe.brushfire

import com.twitter.algebird._

trait LowPriorityDefaults {
  implicit def dispatchedSplitterWithSparseBoolean[A: Ordering, B, C: Ordering, T](
    implicit ordinal: Splitter[A, T],
    nominal: Splitter[B, T],
    continuous: Splitter[C, T],
    sparse: Splitter[Boolean, T]): Splitter[Dispatched[A, B, C, Boolean], T] =
    new DispatchedSplitter(ordinal, nominal, continuous, sparse)
}

trait Defaults extends LowPriorityDefaults {
  implicit def chiSquaredEvaluator[V, L, W <% Double](implicit weightMonoid: Monoid[W]): Evaluator[V, Map[L, W]] = ChiSquaredEvaluator[V, L, W]

  implicit def intSplitter[T: Monoid]: Splitter[Int, T] = BinarySplitter[Int, T](LessThan(_))
  implicit def stringSplitter[T: Monoid]: Splitter[String, T] = BinarySplitter[String, T](EqualTo(_))
  implicit def doubleSplitter[T: Monoid]: Splitter[Double, T] = QTreeSplitter[T](6)
  implicit def booleanSplitter[T: Group]: Splitter[Boolean, T] = SparseBooleanSplitter[T]

  implicit def dispatchedSplitterWithSpaceSaver[A: Ordering, B, C: Ordering, D, L](
    implicit ordinal: Splitter[A, Map[L, Long]],
    nominal: Splitter[B, Map[L, Long]],
    continuous: Splitter[C, Map[L, Long]]): Splitter[Dispatched[A, B, C, D], Map[L, Long]] =
    new DispatchedSplitter(ordinal, nominal, continuous, SpaceSaverSplitter[D, L]())
}
