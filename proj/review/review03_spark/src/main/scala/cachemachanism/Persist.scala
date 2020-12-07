package cachemachanism

import org.apache.spark.storage.StorageLevel

/**
 * @author tiankx
 * @date 2020/11/23 0:02
 * @version 1.0.0
 */
object Persist {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 =
      sc.parallelize(List(("apple", 2), ("apple", 1), ("cat", 4), ("banana", 3), ("scala", 6), ("spark", 8)), 2)
    val result = rdd1
    /** cache 等价于 persist(StorageLevel.MEMORY_ONLY) */
    rdd1.persist() // persist可以根据需要传入缓存级别
    rdd1.persist(StorageLevel.DISK_ONLY)
    rdd1.persist(StorageLevel.MEMORY_AND_DISK)
    result.collect.foreach(println)
    sc.stop()
  }
}
