package com.tian

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

/**
 * @author tiankx
 * @date 2020/11/29 0:46
 * @version 1.0.0
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    //    val params: ParameterTool = ParameterTool.fromArgs(args)
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //    env.getConfig.setGlobalJobParameters(params)
    val text: DataStream[String] = env.readTextFile("files/wordcount.txt")
    val counts: DataStream[(String, Int)] = text
      //      .flatMap((_: String).toLowerCase.split("\\W+")) // import implicits
      //      .filter((_: String).nonEmpty)
      //      .map(((_: String), 1))
      .flatMap(new FlatMapImpl)
      .keyBy(0)
      .sum(1)
    //    if (params.has("output")) {
    //      counts.writeAsText(params.get("output"))
    //    } else {
    //      println("Printing result to stdout. Use --output to specify output path.")
    //      counts.print()
    //    }
    counts.print().setParallelism(1)
    env.execute("Streaming WordCount")
  }

  class FlatMapImpl extends FlatMapFunction[String, (String, Int)] {
    override def flatMap(value: String, out: Collector[(String, Int)]): Unit = {
      val words: Array[String] = value.toLowerCase.split("\\W+")
      for (word <- words if word.length > 0) out.collect((word, 1))
    }
  }

}
