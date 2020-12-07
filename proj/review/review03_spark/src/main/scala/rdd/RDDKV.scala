package rdd

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/28 23:08
 * @version 1.0.0
 */
object RDDKV {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array((10, 1), (20, 2), (30, 3), (40, 4)))
    val rdd2 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 2)
    // TODO: 2020-10-28 23:11:04 spark partitioner
    /**
     * 非键值对类型的RDD一定没有分区器
     * 键值对类型的RDD不一定有分区器
     */
    println(rdd1.partitioner) // None
    println(rdd2.partitioner) // None
    val result = rdd1.partitionBy(new HashPartitioner(3))
    println(result.partitioner)
    //    result.collect.foreach(println)
    sc.stop()
  }
}
