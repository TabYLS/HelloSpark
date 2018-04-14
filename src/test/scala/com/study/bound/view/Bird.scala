package com.study.bound.view

class Bird[T] {

  def fly(t:T): Unit = {
    println(t)
    println("i can fly!")
  }

}
