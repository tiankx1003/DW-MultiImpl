package functions.keyvalue

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/30 0:34
 * @version 1.0.0
 */
object FoldByKey {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("FoldByKey")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array("hello", "world", "hello"))
    val rdd2 = rdd1.map((_, 1)).foldByKey(0)(_ + _)
    /** 结果同上 */
    val rdd3 = rdd1.map((_, 1)).aggregateByKey(0)(_ + _, _ + _)
    sc.stop()
  }
}
