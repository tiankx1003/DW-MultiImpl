package functions.action

/**
 * @author tiankx
 * @date 2020/11/18 20:38
 * @version 1.0.0
 */
object Foreach {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List("hadoop", "scala", "hello", "hello", "scala", "world", "hello", "scala"), 2)
    val result = rdd1
    val arr = Array("aaa", "bbb", "ccc")
    arr.foreach(println)
    val rdd2 = rdd1.collect

    /** 两个foreach不是同一个函数 */
    rdd1.foreach(println)
    rdd1.foreachPartition(it => { // 减少jdbc连接次数， 每个分区获取一次连接
      it.foreach(println)
      it.toList // 会一次把数据加载到driver端，容易造成oom
    })
    result.collect.foreach(println)
    sc.stop()
  }
}
