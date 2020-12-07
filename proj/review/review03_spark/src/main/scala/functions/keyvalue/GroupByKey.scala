package functions.keyvalue

/**
 * @author tiankx
 * @date 2020/11/7 15:58
 * @version 1.0.0
 */
object GroupByKey {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List("hadoop", "scala", "hello", "hello", "scala", "world", "hello", "scala"), 2)
    /** 通过groupByKey实现wordcount */
    val result = rdd1.map((_, 1))
      .groupByKey.map {
      case (key, valueIt) => (key, valueIt.sum)
    }
    result.collect.foreach(println)
    sc.stop()
  }

}
