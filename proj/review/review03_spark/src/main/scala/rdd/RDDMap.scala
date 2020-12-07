package rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/20 14:04
 * @version 1.0.0
 */
object RDDMap {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 3)
    val strRDD = sc.parallelize(Array("tian test", "test tian"))
    val strRDDWithFlatmap = strRDD.flatMap(_.split("\\W+"))
    strRDDWithFlatmap.collect.foreach(println)
    //TODO: 2020-10-27 00:49:10 flatMap与map差异
    /*
    mapPartition对每个分区执行map，会把分区数据放入memory
    数据量大的时候使用map
     */
    //    val result = rdd1.map(x => x * x)
    val result = rdd1.mapPartitions(it => it.map(x => x * x))
    val result2 = rdd1.mapPartitionsWithIndex((index, it) => it.map((index, _))) //索引个数和切片个数一致
    rdd1.groupBy(_ % 2 == 1).map {
      case (k, it) => (k, it.sum)
    }
    val rdd4 = rdd1.groupBy(_ % 3).map {
      case (k, it) => (k, it.sum)
    }
    val rdd5 = rdd1.groupBy(_ % 4) // (k, Iterator)
    //    (0,CompactBuffer(20, 40, 20))
    //    (2,CompactBuffer(30, 50, 50))
    //    rdd5.collect.foreach(println)
    sc.stop()
  }

}
