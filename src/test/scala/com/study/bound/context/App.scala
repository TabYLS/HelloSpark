package com.study.bound.context

object App {

  def main(args: Array[String]): Unit = {
    //上下文界定的第一种写法：隐式的需要一个隐式值：Ordering[T]
    def bigger1[T: Ordering](first: T, second: T): T = {
      if (Ordering[T].compare(first, second) > 0) first else second
    }

    val a = bigger1(55, 77)
    println(a)

    //上下文界定的第二种写法：显式的需要一个隐式值：Ordering[T]
    def bigger2[T](first: T, second: T)(implicit m: Ordering[T]): T = {
      if (m.compare(first, second) > 0) first else second
    }

    val b = bigger2(88, 55)
    println(b)

  }

}
