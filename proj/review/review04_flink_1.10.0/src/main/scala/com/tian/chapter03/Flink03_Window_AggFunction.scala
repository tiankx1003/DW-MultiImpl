package com.tian.chapter03

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


/**
 * @author tiankx
 * @date 2020/12/6 17:57
 * @version 1.0.0
 */
object Flink03_Window_AggFunction {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val socketSource: DataStream[String] = env.socketTextStream("localhost", 9999)
    val result = socketSource
      .map(data => {
        val splits: Array[String] = data.split(",")
        SensorReading(splits(0).trim, splits(1).trim.toLong, splits(2).trim.toInt)
      })
      //.map((_.id,1))
      .map(data => (data.id, 1))
      .keyBy(0)
      .timeWindow(Time.seconds(10))
      // 使用窗口的增量函数进行聚合
      .reduce(new ReduceFunction[(String, Int)] {
        override def reduce(t: (String, Int), t1: (String, Int)) = (t._1, t._2 + t1._2)
      })
    result.print()
    env.execute("Flink03_Window_AggFunction")


  }

  /**
   *
   * @param id
   * @param timestamp
   * @param waterLevel
   */
  case class SensorReading(id: String, timestamp: Long, waterLevel: Int)

}
