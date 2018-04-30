package com.study.spark.url

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

object AdvUrlCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UserLocation").setMaster("local[2]")
    val sc   = new SparkContext(conf)
    //从数据库中加载规则,这里先暂时用数组
    val arr = Array("java.itcast.cn", "php.itcast.cn", "net.itcast.cn")
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
    for (ins <- arr) {
      //这里就没有再使用scala集合自带的方法，而是使用rdd中的方法，这样的好处是不会出现内存溢出的情况，
      // 因为rdd在内存装不下的时候会把数据持久化到磁盘
      val filters = rdd3.filter(_._1 == ins)
      val result = filters.sortBy(_._3, false).take(3)
      println(result.toBuffer)
    }
  }

}
