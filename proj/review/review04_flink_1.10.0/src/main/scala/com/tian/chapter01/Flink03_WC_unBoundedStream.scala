package com.tian.chapter01

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @author tiankx
 * @date 2020/12/3 23:20
 * @version 1.0.0
 */
object Flink03_WC_unBoundedStream {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val sockDS: DataStream[String] = env.socketTextStream("localhost", 9999)
    val result: DataStream[(String, Int)] = sockDS
      .flatMap(_.split("\\W+"))
      .map((_, 1))
      .filter(_._1=="aa")

      .keyBy(0)
      .sum(1)
    result.print().setParallelism(1)
    env.execute()

  }
}
