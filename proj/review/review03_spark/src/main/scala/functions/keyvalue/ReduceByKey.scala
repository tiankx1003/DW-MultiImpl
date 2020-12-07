package functions.keyvalue

/**
 * @author tiankx
 * @date 2020/11/7 16:01
 * @version 1.0.0
 */
object ReduceByKey {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List("hadoop", "scala", "hello", "hello", "scala", "world", "hello", "scala"), 2)
    val result = rdd1.map((_,1)).reduceByKey(_+_)
    rdd1.map((_,1)).reduceByKey(_+_,2) // 传入分区个数
    /**
     * reduceByKey
     * 把key相同的value放在一起reduce，默认使用Hash分区，可以传入分区个数，会有预聚合，然后shuffle，提升了性能
     * groupByKey
     * 只有分区，然后就是shuffle
     * 业务允许时，优先使用reduceByKey，要求预集合逻辑和聚合逻辑一致
     * Hive map端聚合？？？
     */
    result.collect.foreach(println)
    sc.stop()
  }
}
