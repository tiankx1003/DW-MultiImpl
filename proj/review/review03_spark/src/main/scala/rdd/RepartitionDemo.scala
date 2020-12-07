package rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/28 0:17
 * @version 1.0.0
 */
object RepartitionDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 2)

    val result = rdd1.repartition(10) //repartition底层调用coalesce(numPartitons,true)
    println(rdd1.coalesce(1).getNumPartitions)
    println(rdd1.coalesce(10).getNumPartitions) //shuffle为false时增大分区个数不生效
    println(rdd1.coalesce(10, true).getNumPartitions)
    //    result.collect.foreach(println)
    sc.stop()
  }
}
