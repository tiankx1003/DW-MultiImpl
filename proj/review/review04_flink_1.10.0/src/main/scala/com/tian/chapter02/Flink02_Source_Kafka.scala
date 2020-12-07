package com.tian.chapter02

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import java.util.Properties

/**
 * @author tiankx
 * @date 2020/12/6 9:26
 * @version 1.0.0
 */
object Flink02_Source_Kafka {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val prop = new Properties()
    /** 配置kafka信息 */
    prop.setProperty("bootstrap.server","server01:9092")
  //  TODO 2020-12-6 15:14:33 验证Kafka Source

  }
}
