package com.tian

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.util.Collector


/**
 * @author tiankx
 * @date 2020/11/28 23:02
 * @version 1.0.0
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    //val params: ParameterTool = ParameterTool.fromArgs(args)
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    print("done")

    val text: DataStream[String] = env.readTextFile("files/wordcount.txt")

    val counts: DataStream[(String, Int)] = text
      .flatMap(new Tokenizer)
      .keyBy(0)
      .sum(1)
    counts.print.setParallelism(1)
    env.execute("Streaming WordCount")
  }


  class Tokenizer extends FlatMapFunction[String, (String, Int)] {
    override def flatMap(t: String, collector: Collector[(String, Int)]): Unit = {
      val tokens: Array[String] = t.toLowerCase.split("\\W+")
      for (elem <- tokens if elem.nonEmpty) collector.collect((elem, 1))
    }
  }

}


