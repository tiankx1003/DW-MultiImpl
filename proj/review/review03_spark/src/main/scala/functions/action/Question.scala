package functions.action

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/11/18 20:51
 * @version 1.0.0
 */
object Question {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Question").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array("hello", "world", "scala", "spark", "hello"))
    rdd1.map(x => {
      println(x)
      (x, 1)
    }).reduceByKey(_ + _).sortByKey()

    /** sortByKey 会创建RangePartitioner，而创建RangePartitioner时会进行水塘抽样，在水塘抽样时有collect */
    sc.stop()
  }
}
