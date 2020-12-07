package com.tian.chapter01

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

/**
 * @author tiankx
 * @date 2020/12/4 8:49
 * @version 1.0.0
 */
object Flink04_WC_Parallelism {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val text: DataStream[String] = env.readTextFile("files/wordcount.txt")
    val result: DataStream[(String, Int)] = text
      //.flatMap(_.split("\\W+")).setParallelism(2)
      .flatMap(new MyFlatMap())
      //.map((_, 1)).setParallelism(1)
      .keyBy(0)
      .sum(1).setParallelism(3)
    result.print().setParallelism(1)
    env.execute()
  }

  class MyFlatMap extends FlatMapFunction[String, (String, Int)] {
    override def flatMap(t: String, collector: Collector[(String, Int)]): Unit = {
      for (word <- t.split("\\W+") if word.nonEmpty) collector.collect((word, 1))
    }
  }

}
