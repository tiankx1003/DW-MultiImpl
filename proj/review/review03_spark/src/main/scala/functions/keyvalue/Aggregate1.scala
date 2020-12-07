package functions.keyvalue

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/30 0:01
 * @version 1.0.0
 */
object Aggregate1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("aggregate").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array("hello", "hello", "world"),2)
    val rdd2 = rdd1.map((_, 1))
    val result = rdd2.aggregateByKey(0)(_ + _, _ + _) //两段逻辑为分区内聚合，分区间聚合
    result.collect.foreach(println)
    sc.stop()
  }
}
