package com.tian.chapter01

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @author tiankx
 * @date 2020/12/3 22:59
 * @version 1.0.0
 */
object Flink02_WC_BoundedStream {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val text: DataStream[String] = env.readTextFile("files/wordcount.txt")
    val result: DataStream[(String, Int)] = text
      .flatMap(_.split("\\W+"))
      .map((_, 1))
      .keyBy(0)
      .sum(1)
    result.print().setParallelism(1)
    env.execute()
  }
}
