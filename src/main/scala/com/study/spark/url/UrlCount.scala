package com.study.spark.url

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

/**
  *  根据指定的学科, 取出点击量前三的
  */
object UrlCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UserLocation").setMaster("local[2]")
    val sc   = new SparkContext(conf)
    val rdd1 = sc.textFile("/home/yls/workspace/HelloSpark/src/data/url/")
    //得到(url, count)
    val rdd2 = rdd1.map(line => {
      val fields = line.split("\t")
      val url = fields(1)
      (url, 1)
    }).reduceByKey(_ + _)
    println(rdd2.collect().toBuffer)
    //取出学科（host）得到(host, url, count)
    val rdd3 = rdd2.map(x => {
      val url = x._1
      val host = new URL(url).getHost
      (host, url, x._2)
    })
    println(rdd3.collect().toBuffer)

    //把同一学科的放在同一个分组
    val rdd4 = rdd3.groupBy(_._1)
    println(rdd4.collect().toBuffer)

    val rdd5 = rdd4.mapValues(it => {
      it.toList.sortBy(_._3).reverse.take(3)
    })
    println(rdd5.collect().toBuffer)
    sc.stop()
  }

}
