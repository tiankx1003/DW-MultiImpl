package com.tian.chapter03

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, createTypeInformation}

/**
 * @author tiankx
 * @date 2020/12/6 17:54
 * @version 1.0.0
 */
object Flink02_Window_CountWindow {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val socketStream = env.socketTextStream("localhost", 9999)
    socketStream
      .flatMap(_.split("\\W+"))
      .map((_, 1))
      .keyBy(0)
      .countWindow(3, 1)
      .sum(1)
      .print()
    env.execute()
  }
}
