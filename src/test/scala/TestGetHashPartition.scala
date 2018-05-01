object TestGetHashPartition {

  def main(args: Array[String]): Unit = {
    val mod = 3;
    val arr = Array("java.itcast.cn", "php.itcast.cn", "net.itcast.cn")
    for (x <- arr) {
      val rawMod = x.hashCode % mod
      val partitionNum = rawMod + (if (rawMod < 0) mod else 0)
      println(partitionNum)
    }

  }

}
