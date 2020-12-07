package com.tian.chapter01

import org.apache.flink.api.scala.{AggregateDataSet, DataSet, ExecutionEnvironment, createTypeInformation}

/**
 * @author tiankx
 * @date 2020/12/3 22:52
 * @version 1.0.0
 */
object Flink01_WC_Batch {
  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val text: DataSet[String] = env.readTextFile("files/wordcount.txt")
    val result: AggregateDataSet[(String, Long)] = text
      .flatMap(_.split("\\W+"))
      .map((_, 1L))
      .groupBy(0)
      .sum(1)
    result.print() // DataSet.print()函数自带execute
//    env.execute()
  }
}
