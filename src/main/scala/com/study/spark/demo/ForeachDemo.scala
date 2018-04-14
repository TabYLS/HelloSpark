package com.study.spark.demo

import org.apache.spark.{SparkConf, SparkContext}

object ForeachDemo {

  def main(args: Array[String]): Unit = {
    //setMaster可以指定运行模式，local表示本地运行，local[2]表示开启两个线程进行计算，默认是1
    val conf = new SparkConf().setAppName("ForeachDemo").setMaster("local")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 3)
    //rdd1.foreach(println(_))
    //注意：Iterator的map和List的map不一样，以后深入
    rdd1.map(x => {
      println(s"#############$x")
    })
    rdd1.foreachPartition(it => {
      it.map(println(_))
      println(it.next())
      println("ddd")
      println(it.hasNext)
    })
    sc.stop()
  }

}
