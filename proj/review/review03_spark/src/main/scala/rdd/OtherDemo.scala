package rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/28 0:25
 * @version 1.0.0
 */
object OtherDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 2)
    val filteredRDD = rdd1.filter(_ % 2 == 0)
    rdd1.filter(x => (x & 1) == 1)
    val result = rdd1.distinct
    val rdd2 = rdd1.distinct(20) //如果是自定义类需要提供Ordering类型隐式值
    println(rdd2.getNumPartitions)
    result.collect.foreach(println)
    sc.stop()
  }
}
