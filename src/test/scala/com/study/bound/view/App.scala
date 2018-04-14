package com.study.bound.view

object App {

  def main(args: Array[String]): Unit = {

    //视图界定的写法一：隐式需要一个隐式视图T=>Ordered[T]
    def bigger1[T <% Ordered[T]](first:T, second:T): T ={
      if(first > second) first else second
    }
    val a1 = bigger1(20,50)
    println(a1)

    //视图界定的写法二：显示的指定需要一个隐式视图T=>Ordered[T]
    def bigger2[T](first:T, second:T)(implicit f: T=>Ordered[T]): T ={
      if(first > second) first else second
    }
    val a2 = bigger2(60, 59)
    println(a2)
  }

}
