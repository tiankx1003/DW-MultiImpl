package functions.action

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/28 23:34
 * @version 1.0.0
 */
object CountByKey {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List("hadoop", "scala", "hello", "hello", "scala", "world", "hello", "scala"), 2)
    val rdd2 = rdd1.map(x => {
      println(x)
      (x, 1)
    }) //转换算子在行动算子之前不会被执行
    val rdd3 = rdd2.reduceByKey(_ + _)
    val rdd4 = rdd2.countByKey() //计算每个key对应的个数
    val rdd5 = rdd2.countByValue() //计算每个value对应的个数
    rdd5.foreach(println)
    rdd1.collect.foreach(println)
//    rdd5.collect.foreach(println) //报错
//    rdd5.collect

    sc.stop()
  }
}
