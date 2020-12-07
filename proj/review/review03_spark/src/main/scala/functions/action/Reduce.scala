package functions.action

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/11/18 20:46
 * @version 1.0.0
 */
object Reduce {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(3, 4, 5, 6))
    println(rdd1.reduce(_ + _))

    /** 零值参与运算的次数是分区数+1 */
    val result1 = rdd1.aggregate(0)(_ + _, _ + _)
    val result2 = rdd1.aggregate(10)(_ + _, _ + _) // 分区合并也会使用零值
    println(result2 - result1) // zeroValue * 参与运算的次数
    sc.stop()
  }
}
