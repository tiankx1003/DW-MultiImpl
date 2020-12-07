package functions

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/11/19 19:20
 * @version 1.0.0
 */
object DoubleValue {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("DoubleValue").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50))
    val rdd2 = sc.parallelize(Array(20, 30, 40, 50))
    rdd1.union(rdd2)
    rdd1 ++ rdd2 // 同上，取并集
    rdd1.subtract(rdd2) // 差集
    rdd1.intersection(rdd2) // 交集
    rdd1.cartesian(rdd2) // 笛卡尔积
    rdd1.zip(rdd2) // 拉链，要求分区个数一致，每个分区内的元素相同
    rdd1.zipWithIndex()
    rdd1.zipPartitions(rdd2)((it1, it2) => it1.zip(it2))
    rdd1.zipPartitions(rdd2)((it1, it2) => it1.zipAll(it2, 100, 200))
    sc.stop()
  }
}
