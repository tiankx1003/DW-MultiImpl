package functions.keyvalue


import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/30 0:25
 * @version 1.0.0
 */
object Join {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("Join").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array((1, "a"), (2, "b"), (3, "c")))
    val rdd2 = sc.parallelize(Array((1, "A"), (2, "B"), (3, "C")))
    val rdd3 = rdd1.join(rdd2)
    val rdd4 = rdd1.leftOuterJoin(rdd2)
    val rdd5 = rdd1.rightOuterJoin(rdd2)
    rdd3.collect.foreach(println)
    sc.stop()

  }

}
