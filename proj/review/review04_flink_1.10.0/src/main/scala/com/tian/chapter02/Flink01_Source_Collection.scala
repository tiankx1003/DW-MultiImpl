package com.tian.chapter02

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @author tiankx
 * @date 2020/12/6 9:17
 * @version 1.0.0
 */
object Flink01_Source_Collection {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val sensorList = List(WaterSensor("01", 1, 1), WaterSensor("02", 2, 2), WaterSensor("03", 3, 3))
    val sensorDS: DataStream[WaterSensor] = env.fromCollection(sensorList)
    sensorDS.print()
    env.execute("collection ds")
  }

  /**
   * 水位传感器
   *
   * @param id 编号
   * @param ts 时间戳
   * @param vc 空高
   */
  case class WaterSensor(id: String, ts: Int, vc: Int)

}
