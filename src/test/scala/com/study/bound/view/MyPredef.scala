package com.study.bound.view

import scala.math.Ordering

object MyPredef {

  //隐式转换，按照年龄比较
  implicit val boy2Ordering = new Ordering[Boy] {
    override def compare(x: Boy, y: Boy): Int = {
      x.age - y.age
    }
  }
/*  implicit val boy2Ordering1 = ()=> new Ordering[Boy] {
    override def compare(x: Boy, y: Boy): Int = {
      x.age - y.age
    }
  }*/

/*
  implicit val boy2Ordering1 = (x: Boy)=> new Ordered[Boy] {
    override def compare(y: Boy): Int = {
      x.age - y.age
    }
  }
*/


  /*implicit def boy2Ordered(boy: Boy) = new Ordered[Boy]{
    override def compare(that: Boy): Int = {
      if(boy.age == that.age){
        boy.high - that.high
      } else {
        boy.age - that.age
      }
    }
  }

  //模仿Int的写法
  trait Boy2Ordering extends Ordering[Boy] {
    def compare(x: Boy, y: Boy) =
      if (x.age < y.age) -1
      else if (x.age == y.age) 0
      else 1
  }

  implicit object B extends Boy2Ordering*/


 /* trait IntOrdering extends Ordering[Int] {
    def compare(x: Int, y: Int) =
      if (x < y) -1
      else if (x == y) 0
      else 1
  }
  implicit object I extends IntOrdering*/


}



