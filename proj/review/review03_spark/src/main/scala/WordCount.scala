import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/10 17:17
 * @version 1.0.0
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val rdd = sc.textFile("./files/wordcount.txt") //(args(0))
      .flatMap(_.split("\\W+"))
      .map((_, 1))
      .reduceByKey(_ + _)
    rdd.collect.foreach(println)
    sc.stop()
  }
} 
