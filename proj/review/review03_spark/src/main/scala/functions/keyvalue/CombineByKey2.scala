package functions.keyvalue

import org.apache.log4j.{Level, Logger}

/**
 * @author tiankx
 * @date 2020/10/31 10:17
 * @version 1.0.0
 */
object CombineByKey2 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List(("apple", 2), ("apple", 1), ("cat", 4), ("banana", 3), ("scala", 6), ("spark", 8)), 2)
    //(key, sum(value), count(key))
    val result = rdd1.combineByKey(
      v => (v, 1),
      (sumCount: (Int, Int), v: Int) => (sumCount._1 + v, sumCount._2 + v), // (v+v,1+1)
      (sumCount1: (Int, Int), sumCount2: (Int, Int)) => (sumCount1._1 + sumCount2._1, sumCount1._2 + sumCount2._2)
    )
    result.collect.foreach(println)
    sc.stop()
  }
}
