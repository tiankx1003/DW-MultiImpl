package com.tian.chapter01

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @author tiankx
 * @date 2020/12/6 9:08
 * @version 1.0.0
 */
object Flink05_WC_OperatorChains {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val socktDS: DataStream[String] = env.socketTextStream("localhost", 9999)
    val result: DataStream[(String, Int)] = socktDS.flatMap(_.split("\\W+")).setParallelism(2)
      .map((_, 1)).setParallelism(2)
      //.startNewChain()
      //.disableChaining()
      .keyBy(0)
      .sum(1)
    result.print()
    env.execute("operator chains")
  }
}
