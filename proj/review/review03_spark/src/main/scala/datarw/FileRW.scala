package datarw

import org.apache.spark.{SparkConf, SparkContext}

import scala.util.parsing.json.JSON

/**
 * @author tiankx
 * @date 2020/11/23 0:11
 * @version 1.0.0
 */
object FileRW {
  System.setProperty("HADOOP_USER_NAME","tian") // 也可以通过properties文件设置
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("textfile_rw").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val textfileRDD = sc.textFile("file/test.txt")
    val jsonRDD = sc.textFile("file/test.json")
    val parseJsonRDD = jsonRDD.map(JSON.parseFull)
    val strRDD = sc.parallelize(Array(("apple", 2), ("apple", 1)))
    strRDD.saveAsSequenceFile("hdfs://server01:9000/seqFiles")
    val seqRDD = sc.sequenceFile[String, Int]("hdfs://server01:9000/seqFiles/seq")
    val objRDD = sc.objectFile("hdfs://server01:9000/seqFile/objfile")
    objRDD.saveAsObjectFile("hdfs//server01:9000/seqFile/objfile2")
    sc.stop()
  }
}
