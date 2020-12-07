package rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * RDD创建
 * 1. 集合
 * 2. 外部存储
 * 3. 其他RDD转换
 *
 * @author tiankx
 * @date 2020/10/20 14:02
 * @version 1.0.0
 */
object CreateRDD {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array((10, 1), (20, 2), (30, 3), (40, 4)))
    val result = rdd1
    result.collect.foreach(println)
    sc.stop()
  }

}
