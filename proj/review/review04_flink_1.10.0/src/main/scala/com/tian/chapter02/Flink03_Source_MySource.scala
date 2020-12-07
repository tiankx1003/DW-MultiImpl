package com.tian.chapter02

import com.tian.chapter02.Flink01_Source_Collection.WaterSensor
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import scala.util.Random

/**
 * @author tiankx
 * @date 2020/12/6 9:32
 * @version 1.0.0
 */
object Flink03_Source_MySource {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val sourceDS: DataStream[WaterSensor] = env.addSource(new MySourceFunction)
    sourceDS.print()
    env.execute()
  }

  class MySourceFunction extends SourceFunction[WaterSensor] {
    var flag = true

    override def run(sct: SourceFunction.SourceContext[WaterSensor]): Unit = {
      while (flag)
        sct.collect(WaterSensor(
          "sensor" + (Random.nextInt(3) + 1),
          System.currentTimeMillis().toInt,
          Random.nextInt(10))
        )
      Thread.sleep(1000L)
    }

    override def cancel(): Unit = flag = false
  }

}
