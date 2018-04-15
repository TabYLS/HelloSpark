package com.study.spark.demo

import org.apache.spark.{SparkConf, SparkContext}

object UserLocation {

  def main(args: Array[String]): Unit = {
    //setMaster可以指定运行模式，local表示本地运行，local[2]表示开启两个线程进行计算，默认是1
    val conf = new SparkConf().setAppName("UserLocation").setMaster("local[2]")
    val sc = new SparkContext(conf)

    /*将文件中的数据读取出来，然后拼接：(手机号码,时间,基站，触发的事件（出站还是入站）)
    这样写耗费比较多的资源，两次map
      val srcData = sc.textFile("/home/yls/workspace/HelloSpark/src/data/userLocation/bs_log")
      .map(_.split(",")).map(x => ((x(0), x(1), x(2), x(3))))
    println(srcData.collect().toBuffer)
    */


    //mobile_location,time
    //ArrayBuffer((18688888888_16030401EAFB68F1E3CDF819735E1C66,-20160327082400),....)
    val m_l_t = sc.textFile("/home/yls/workspace/HelloSpark/src/data/userLocation/bs_log")
      .map(x => {
        val fields = x.split(",")
        val eventType = fields(3)
        //因为在每个呆的时间长短都是出站时间减去入站的时间，当进站的时候全部设为负数，出站的时候全部设为正数
        //后面计算的时候只需要分组相加即可
        val timeLong = if (eventType == "0") fields(1).toLong else -fields(1).toLong
        (fields(0) + "_" + fields(2), timeLong)
      })
    println(m_l_t.collect().toBuffer)

    //分组
    //ArrayBuffer((18611132889_9F36407EAD0629FC166F14DDE7970F68,CompactBuffer((18611132889_9F36407EAD0629FC166F14DDE7970F68,-20160327075000),...),....)
    val rdd1 = m_l_t.groupBy(_._1)
    println(rdd1.collect().toBuffer)
    //将同一组的value的时间相加，得出每个用户在每个基站的停留时间
    //ArrayBuffer((18611132889_9F36407EAD0629FC166F14DDE7970F68,54000),....)
    val rdd2 = rdd1.mapValues(_.foldLeft(0L)(_ + _._2))
    println(rdd2.collect().toBuffer)



    sc.stop()
  }
}
