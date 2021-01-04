package com.tian.chapter03

import com.tian.chapter03.Flink03_Window_AggFunction.SensorReading
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * @author tiankx
 * @date 2020/12/10 20:587
 * @version 1.0.0
 */
object Flink06_TimeCharacteristic_ProcessingTime {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val socketSource: DataStream[String] = env.socketTextStream("localhost", 9999)
    val sensorStream: DataStream[SensorReading] = socketSource.map(data => {
      val splits: Array[String] = data.split(",")
      SensorReading(splits(0).trim, splits(1).trim.toLong, splits(2).trim.toInt)
    })
    sensorStream
      .map(data => (data.id, 1))
      .keyBy(_._1)
      .timeWindow(Time.seconds(5))
      .sum(1)
      .print()

    env.execute("Flink06_TimeCharacteristic_ProcessingTime")
  }
}
