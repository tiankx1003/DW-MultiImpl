package practice

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/11/19 19:17
 * @version 1.0.0
 */
object Practice2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice2").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val line = sc.textFile("file/agent.log")
    line.map(line => {
      val words = line.split("\\W+")
      words
    })
  }
}
