package com.study.spark.userLocation

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 这个类是为了测试reduceByKey，因为视频上说不可以使用，所以来测试一下，发现是可以得到正确结果的，合乎逻辑，没毛病，视频讲的不对
  */
object UserLocation2 {

  def main(args: Array[String]): Unit = {
    //setMaster可以指定运行模式，local表示本地运行，local[2]表示开启两个线程进行计算，默认是1
    val conf = new SparkConf().setAppName("UserLocation").setMaster("local[2]")
    val sc   = new SparkContext(conf)

    val m_l_t = sc.textFile("/home/yls/workspace/HelloSpark/src/data/userLocation/bs_log")
      .map(x => {
        val fields    = x.split(",")
        val eventType = fields(3)
        //因为在每个呆的时间长短都是出站时间减去入站的时间，当进站的时候全部设为负数，出站的时候全部设为正数
        //后面计算的时候只需要分组相加即可
        val timeLong  = if (eventType == "0") fields(1).toLong else -fields(1).toLong
        (fields(0) + "_" + fields(2), timeLong)
      })
    //ArrayBuffer((18611132889_9F36407EAD0629FC166F14DDE7970F68,54000),
    val rdd1 = m_l_t.reduceByKey(_ + _)
    println("###############"+ rdd1.collect().toBuffer)
    val rdd2 = rdd1.map(x =>{
      val mobile = x._1.split("_")(0)
      val lac    = x._1.split("_")(1)
      val time   = x._2
      (mobile, lac, time)
    })
    //ArrayBuffer((18688888888,CompactBuffer((18688888888,9F36407EAD0629FC166F14DDE7970F68,51200)
    val rdd3 = rdd2.groupBy(_._1)
    println(rdd3.collect().toBuffer)
    /*val rdd4 = rdd3.mapValues(it =>{
      val v = it.toList
      val sort = v.sortBy(_._3).reverse.take(2)
      sort
    })*/
    val rdd4 = rdd3.map(it =>{
      val v = it._2.toList
      val sort = v.sortBy(_._3).reverse.take(2)
      sort
    })
    println(rdd4.collect().toBuffer)
    sc.stop()

  }

}
