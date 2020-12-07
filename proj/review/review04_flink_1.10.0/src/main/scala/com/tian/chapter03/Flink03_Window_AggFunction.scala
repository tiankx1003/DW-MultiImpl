package com.tian.chapter03

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}


/**
 * @author tiankx
 * @date 2020/12/6 17:57
 * @version 1.0.0
 */
object Flink03_Window_AggFunction {
  def main(args: Array[String]): Unit = {
    //val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //val socketSource: DataStream[String] = env.socketTextStream("localhost", 7777)
    //import org.apache.flink.streaming.api.scala._
    //val sensorStream: DataStream[SensorReading] = socketSource.map(data => {
    //    val splits: Array[String] = data.split(",")
    //    SensorReading(splits(0).trim, splits(1).trim.toLong, splits(2).trim.toDouble)
    //})
    //sensorStream
    //
    //env.execute("Flink03_Window_AggFunction")
  }

  case class SensorReading(id : String, timestamp: Long, waterLevel: Double)

}
