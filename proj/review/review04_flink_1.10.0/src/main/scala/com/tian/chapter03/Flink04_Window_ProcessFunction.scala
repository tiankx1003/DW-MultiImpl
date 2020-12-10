package com.tian.chapter03

import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
 * @author tiankx
 * @date 2020/12/7 14:01
 * @version 1.0.0
 */
object Flink04_Window_ProcessFunction {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val socketSource: DataStream[String] = env.socketTextStream("localhost", 7777)
    val result = socketSource
      .map((_, 1))
      .keyBy(_._1)
      .timeWindow(Time.seconds(3))
      .process(new ProcessWindowFunction[(String, Int), String, String, TimeWindow] {
        override def process(key: String, context: Context, elements: Iterable[(String, Int)], out: Collector[String]): Unit = {
          out.collect("current_key="
            + key
            + ", belong to window:["
            + context.window.getStart
            + ","
            + context.window.getEnd
            + "], total:"
            + elements.size)
        }
      })
    result.print()
    env.execute("Flink04_Window_ProcessFunction")
  }
}
