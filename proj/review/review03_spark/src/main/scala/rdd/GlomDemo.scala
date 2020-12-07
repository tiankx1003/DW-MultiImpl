package rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/28 0:42
 * @version 1.0.0
 */
object GlomDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array("tian test", "test scala", "scala test", "spark scala"), 4)
    //Return an RDD created by coalescing all elements within each partition into an array.
    //把每个分区内的集合组成数组
    val result = rdd1.glom()
    result.collect.foreach(x => println(x.mkString(",")))
    sc.stop()
  }
}
