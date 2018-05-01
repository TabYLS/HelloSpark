package com.study.spark.demo

import org.apache.spark.{SparkConf, SparkContext}

object TestCombineByKey {

  def main(args: Array[String]): Unit = {
    test2()
  }

  def  test1(): Unit = {
    val conf = new SparkConf().setAppName("CombineByKey").setMaster("local[2]")
    val sc   = new SparkContext(conf)
    val rdd1 = sc.parallelize(List("dog","cat","gnu","salmon","rabbit","turkey","wolf","bear","bee"), 3)
    val rdd2 = sc.parallelize(List(1,1,2,2,2,1,2,2,2), 3)
    val rdd3 = rdd2.zip(rdd1)
    println(rdd3.collect().toBuffer)
    val rdd4 = rdd3.combineByKey(List(_), (x: List[String], y: String) => x :+ y, (m: List[String], n: List[String]) => m ++ n)
    println(rdd4.collect().toBuffer)
    sc.stop()
  }

  def test2(): Unit = {
    val conf = new SparkConf().setAppName("CombineByKey").setMaster("local[1]")
    val sc   = new SparkContext(conf)
    val rdd1 = sc.makeRDD(Array(("A", 1), ("A", 2), ("B", 3), ("B", 1), ("B", 2), ("C", 1)), 2)
    //val rdd1 = sc.parallelize(Array(("A", 1), ("A", 2), ("B", 1), ("B", 2), ("C", 1)), 2)
    val rdd2 = rdd1.combineByKey(
      (v: Int) => v +"_",
      (c: String, v:Int) => c + "@" + v,
      (c1: String, c2: String) => c1 + "$" +c2
    )
    println(rdd2.collect().toBuffer)
    sc.stop()
  }

}
