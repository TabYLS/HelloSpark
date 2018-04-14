package com.study.bound.context

/*class ContextBound[T: Ordered] {

  def bigger(first: T, second: T): T = {
    if (Ordering[T].compare(first, second) > 0) first else second
  }

}*/

/*class ContextBound[T: Ordering] {
  def bigger(first: T, second: T): T = {
    val ord = implicitly[Ordering[T]]
    if (ord.gt(first, second)) first else second
  }
}*/
class ContextBound[T] {
  def bigger1(first: T, second: T)(implicit ord:Ordering[T]): T = {
    //val ord = implicitly[Ordering[T]]
    if (ord.gt(first, second)) first else second
  }

  //传入的Ordering，但是想使用Ordered的方法
  def bigger2(first: T, second: T)(implicit ord:Ordering[T]): T = {
    import Ordered.orderingToOrdered
    if (first > second) first else second
  }
}

object ContextBound {
  def main(args: Array[String]): Unit = {
    val g1 = new Gir("张敏", 25, 170)
    val g2 = new Gir("邱淑贞", 25, 175)
    import MyPredef._
    val c = new ContextBound[Gir]
    //val bigger = c.bigger1(g1, g2)
    val bigger = c.bigger2(g1, g2)
    println(bigger)
  }
}
