package cachemachanism

/**
 * @author tiankx
 * @date 2020/11/23 0:07
 * @version 1.0.0
 */
object Checkpoint {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List(("apple", 2), ("apple", 1), ("cat", 4), ("banana", 3), ("scala", 6), ("spark", 8)), 2)
    val result = rdd1
    result.checkpoint() // checkpoint会截断血缘关系，之后的执行算子会立即启动一个job用于checkpoint
    /** SparkContext.setCheckpointDir() 可以设置存储检查点的目录 */
    result.collect.foreach(println)
    sc.stop()
  }
}

/**
 * 持久化和检查点的区别
 */
