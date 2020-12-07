package cachemachanism

/**
 * @author tiankx
 * @date 2020/11/23 0:00
 * @version 1.0.0
 */
object Cache {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List(("apple", 2), ("apple", 1), ("cat", 4), ("banana", 3), ("scala", 6), ("spark", 8)), 2)
    val result = rdd1
    result.cache() // 计算结果放入缓存
    result.collect.foreach(println)
    sc.stop()
  }
}
