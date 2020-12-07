package functions.keyvalue

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/30 0:07
 * @version 1.0.0
 */
object Aggregate2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("aggregate2").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd2 = sc.parallelize(Array("hello", "world", "hello"), 2)
    val rdd1 = sc.parallelize(Array(1, 2, 3, 4, 5))
    rdd1.aggregate(0)(math.max, _ + _)
    rdd1.aggregate(Int.MaxValue)((x, y) => x.max(y), _ + _)
    sc.stop()
  }
}
