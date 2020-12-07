package functions.keyvalue

import org.apache.log4j.{Level, Logger}

/**
 * @author tiankx
 * @date 2020/10/30 22:00
 * @version 1.0.0
 */
object CombineByKey1 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List(("apple", 2), ("apple", 1), ("cat", 4), ("banana", 3), ("scala", 6), ("spark", 8)), 2)

    /** combineByKey与aggregateByKey相比，不写明零值，可通过第一次计算得到 */
    val result = rdd1.combineByKey(
      v => v, // 初始化
      (last: Int, v: Int) => last + v, // 每个分区的分区内执行，last + v 作为last
      (v1: Int, v2: Int) => v1 / v2 // 两个分区之间的合并策略
    )
    result.collect.foreach(println)
    sc.stop()
  }
}
