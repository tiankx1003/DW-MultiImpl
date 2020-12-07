package functions.keyvalue

/**
 * @author tiankx
 * @date 2020/10/31 10:32
 * @version 1.0.0
 */
object CoGroup1 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array((1, "aa"), (2, "bb"), (3, "cc")))
    val rdd2 = sc.parallelize(Array((1, "aa"), (2, "bb"), (3, "cc")))
    val result = rdd1.cogroup(rdd2)
    result.collect.foreach(println)
    sc.stop()
  }
}
