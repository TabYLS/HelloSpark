package com.study.spark.userLocation

import org.apache.spark.{SparkConf, SparkContext}

object AdvUserLocation {

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
        ((fields(0), fields(2)), timeLong)
      })
    //将(mobile,lac)相同的进行分组然后时间相加
    //ArrayBuffer(((18688888888,CC0710CC94ECC657A8561DE549D940E0),1300), ((18611132889,9F36407EAD0629FC166F14DDE7970F68),54000),
    val rdd1 = m_l_t.reduceByKey(_ + _)
    println(rdd1.collect.toBuffer)
    //转换，将lac作为key
    //ArrayBuffer((CC0710CC94ECC657A8561DE549D940E0,(18688888888,1300)),
    val rdd2 = rdd1.map(x => {
      val mobile = x._1._1
      val lac    = x._1._2
      val time   = x._2
      (lac, (mobile, time))
    })
    println(rdd2.collect().toBuffer)

    //读取位置的信息
    val lac_info = sc.textFile("/home/yls/workspace/HelloSpark/src/data/userLocation/lac_info.txt")
      .map(line => {
        val fields   = line.split(",")
        val lac = fields(0)
        val x = fields(1)
        val y = fields(2)
        (lac, (x, y))
      })
    //将位置信息join到userlocation当中
    //ArrayBuffer((CC0710CC94ECC657A8561DE549D940E0,((18688888888,1300),(116.303955,40.041935))), (CC0710CC94ECC657A8561DE549D940E0,((18611132889,1900),(116.303955,40.041935))),
    val rdd3 = rdd2.join(lac_info)
    println(rdd3.collect.toBuffer)
    //格式化数据ArrayBuffer((18688888888,CC0710CC94ECC657A8561DE549D940E0,1300,(116.303955,40.041935)),
    val rdd4 = rdd3.map(x =>{
      val lac     = x._1
      val mobile  = x._2._1._1
      val time    = x._2._1._2
      val x_y     = x._2._2
      (mobile, lac, time, x_y)
    })
    println(rdd4.collect.toBuffer)
    //按照手机号码分组
    //ArrayBuffer((18688888888,CompactBuffer((18688888888,CC0710CC94ECC657A8561DE549D940E0,1300,(116.303955,40.041935)),
    val rdd5 = rdd4.groupBy(_._1)
    println(rdd5.collect().toBuffer)
    //格式化数据
    val rdd6 = rdd5.map(x => {
      val mobile = x._1
      val lacs = x._2.toList.reverse.sortBy(_._3).take(2)
      (mobile, lacs)
    })
    println(rdd6.collect().toBuffer)
    rdd6.saveAsTextFile("/home/yls/workspace/output/")
    sc.stop()

  }

}
