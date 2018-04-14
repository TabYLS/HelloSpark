package com.study.bound.view

/*用scala提供的视图界定机制实现视图界定
class ViewBound[T <% Ordered[T]] {

  def bigger(first: T, second: T): T = {
    if (first > second) first else second
  }

}*/

//使用柯里化的方式实现视图界定
class ViewBound[T] {

  def bigger(first: T, second: T)(implicit f: T => Ordered[T]): T = {
    if (first > second) first else second
  }

}

object ViewBound {

  def main(args: Array[String]): Unit = {
    val b1 = new Boy("小明", 21, 175)
    val b2 = new Boy("小李", 21, 165)

    import MyPredef._
    val v = new ViewBound[Boy]
    val boy = v.bigger(b1, b2)
    print(boy)

  }
}
