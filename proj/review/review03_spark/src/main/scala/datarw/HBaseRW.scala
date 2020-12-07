package datarw

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job

/**
 * @author tiankx
 * @date 2020/11/23 20:44
 * @version 1.0.0
 */
object HBaseRW {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val hbaseConf = HBaseConfiguration.create
    hbaseConf.set("hbase.zookeeper.quorum", "server01,server02,server03")
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "student")
    val hbaseRDD = sc.newAPIHadoopRDD(
      hbaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]
    )
    val hbaseRDD2 = hbaseRDD.map {
      case (_, result: Result) => Bytes.toString(result.getRow)
    }
    hbaseRDD2.collect.foreach(println)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 2)
    val result = rdd1
    result.collect.foreach(println)
    hbaseConf.set(TableOutputFormat.OUTPUT_TABLE, "student")
    val job = Job.getInstance(hbaseConf)
//    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Put])
    rdd1.map(x => {
      val put = new Put(Bytes.toBytes(x))
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(x))
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("weight"), Bytes.toBytes(x))
      (new ImmutableBytesWritable(), put)
    }).saveAsNewAPIHadoopDataset(job.getConfiguration)
    sc.stop()
  }
}
