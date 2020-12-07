package functions.keyvalue

/**
 * @author tiankx
 * @date 2020/11/7 16:14
 * @version 1.0.0
 */
object SortByKey {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List(("apple", 2), ("apple", 1), ("cat", 4), ("banana", 3), ("scala", 6), ("spark", 8)), 2)
    val result = rdd1.sortByKey()
    rdd1.sortByKey(false, 4) // 降序，4分区
    rdd1.sortBy(x => x._1) // 底层调用sortByKey
    /** key为自定义类？ 实现compareTo??? */
    result.collect.foreach(println)
    sc.stop()
  }
}
