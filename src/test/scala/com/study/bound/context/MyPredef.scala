package com.study.bound.context

object MyPredef {

  /*implicit val gir2Ordering1 = (x: Gir)=> new Ordered[Gir] {
    override def compare(y: Gir): Int = {
      x.age - y.age
    }
  }*/

  implicit def boy2Ordered(gir: Gir) = new Ordered[Gir]{
    override def compare(that: Gir): Int = {
      if(gir.age == that.age){
        gir.faceValue - that.faceValue
      } else {
        gir.age - that.age
      }
    }
  }

/*  implicit val gir2Ordering2 = new Ordering[Gir] {
    override def compare(x: Gir, y: Gir): Int = {
      y.age - x.age
    }
  }*/

/*  implicit object gir2Ordering3 extends Ordering[Gir] {
    override def compare(x: Gir, y: Gir): Int = {
      y.age - x.age
    }
  }*/

}
