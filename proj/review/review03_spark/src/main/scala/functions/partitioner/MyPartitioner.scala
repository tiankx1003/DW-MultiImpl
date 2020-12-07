package functions.partitioner

import org.apache.spark.Partitioner

/**
 * 自定义分区器
 *
 * @author tiankx
 * @date 2020/11/7 16:35
 * @version 1.0.0
 */
object MyPartitioner {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50, 20, 50, 51), 2)
    val result = rdd1.map((_, null)).partitionBy(new MyPartitioner(2)).map(_._1).glom() //partitionBy是kv类型的算子
    result.collect.foreach(x => println(x.mkString(",")))
    sc.stop()
  }
}

/**
 * 按照奇偶分区
 *
 * @param partitionNum 分区数
 */
class MyPartitioner(var partitionNum: Int) extends Partitioner {
  override def numPartitions: Int = partitionNum

  override def getPartition(key: Any): Int = {
    val k = key.asInstanceOf[Int]

    /** 通过模式识别实现返回 */
    k match {
      case k: Int =>
      case _ =>
    }
    (k % 2).abs // 可能会有负数

  }
}
