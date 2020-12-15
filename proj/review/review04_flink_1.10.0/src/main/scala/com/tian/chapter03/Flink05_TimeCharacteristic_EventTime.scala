package com.tian.chapter03


import com.tian.chapter03.Flink03_Window_AggFunction.SensorReading
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


/**
 * @author tiankx
 * @date 2020/12/10 20:04
 * @version 1.0.0
 */
object Flink05_TimeCharacteristic_EventTime {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val socketSource: DataStream[String] = env.socketTextStream("localhost", 9999)
    /** socket数据需为满足条件的格式 */
    val sensorStream: DataStream[SensorReading] = socketSource.map(data => {
      val splits: Array[String] = data.split(",")
      SensorReading(splits(0).trim, splits(1).trim.toLong, splits(2).trim.toInt)
    })
      .assignAscendingTimestamps(_.timestamp * 1000L) // 提取事件时间戳
    sensorStream
      .map(data => (data.id, 1))
      .keyBy(_._1)
      .timeWindow(Time.seconds(5))
      .sum(1)
      .print()

    env.execute("Flink05_TimeCharacteristic_EventTime")
  }
}
