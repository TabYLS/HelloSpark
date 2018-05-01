package com.study.spark.url

import java.net.URL

import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

/**
  * 将计算分在不同的分区中，不同学院在不同的分区进行计算，结果也按照分区进行保存
  * 但是之前的写法会存在hash碰撞的问题，因此需要重新写分区方法
  * "java.itcast.cn", "php.itcast.cn", "net.itcast.cn"经过hash求值之后都是相同的结果因此产生了冲突(可以通过TestGetHashPartition看到结果)
  *
  */
object UrlCountWithPartition {

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
      (host, (url, x._2))
    })
    println(rdd3.collect().toBuffer)

    //获取所有的学科
    val ints = rdd3.map(x => {
      (x._1)
    }).distinct.collect()
    //把同一学科的放在同一个分区中进行计算
    val hostPartition = new HostPartition(ints)
    val rdd4 = rdd3.partitionBy(hostPartition).mapPartitions(it=>{
      it.toList.sortBy(_._2._2).reverse.take(3).toIterator
    })
    rdd4.saveAsTextFile("/home/yls/workspace/output1/")

    sc.stop()
  }

}

class HostPartition(ins: Array[String]) extends  Partitioner {
  val partMap = new mutable.HashMap[String, Int]()
  var count :Int = 0
  for (i <- ins) {
    partMap += (i -> count)
    count += 1
  }
  override def numPartitions: Int = ins.length

  override def getPartition(key: Any): Int = partMap.getOrElse(key.toString, 0)
}
