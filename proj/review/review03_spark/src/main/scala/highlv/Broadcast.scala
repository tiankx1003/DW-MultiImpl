package highlv

/**
 * @author tiankx
 * @date 2020/11/23 21:29
 * @version 1.0.0
 */
object Broadcast {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 2)
    val set = Set(10, 20)
    val bd = sc.broadcast(set)
    rdd1.foreach(x => println(set.contains(x)))
    rdd1.foreach(x => {
      val set = bd.value
      println(set.contains(x))
    })
    val result = rdd1
    result.collect.foreach(println)
    sc.stop()
  }
}
