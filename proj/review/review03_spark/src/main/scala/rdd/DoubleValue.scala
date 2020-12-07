package rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/28 23:23
 * @version 1.0.0
 */
object DoubleValue {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 2)
    val rdd2 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 2)
    val result = rdd1.union(rdd2)
    val result2 = rdd1 ++ rdd2
    val result3 = rdd1.subtract(rdd2)
    val result4 = rdd1.intersection(rdd2)
    val result5 = rdd1.cartesian(rdd2)
    val result6 = rdd1.zip(rdd2)
    val result7 = rdd1.zipWithIndex()
    /** 要求分区数相同 */
    val result8 = rdd1.zipPartitions(rdd2)((it1, it2) => it1.zip(it2))
    val result9 = rdd1.zipPartitions(rdd2)((it1, it2) => it1.zipAll(it2, 100, 200))


    result.collect.foreach(println)
    sc.stop()
  }
}
