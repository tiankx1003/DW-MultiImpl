package com.tian.chapter03

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, WindowedStream}
import org.apache.flink.streaming.api.windowing.assigners.{ProcessingTimeSessionWindows, SlidingProcessingTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

/**
 * @author tiankx
 * @date 2020/12/6 16:53
 * @version 1.0.0
 */
object Flink01_Window_TimeWindow {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val socketStream = env.socketTextStream("localhost", 9999)
    val dataWS: WindowedStream[(String, Int), String, TimeWindow] = socketStream
      .map(data => (data, 1))
      .keyBy(_._1)
      //      .timeWindow(Time.seconds(3)) // 滚动窗口
      //      .window(TumblingProcessingTimeWindows.of(Time.seconds(3)))

            .timeWindow(Time.seconds(3),Time.seconds(30))  // 滑动窗口，多传一个 滑动间隔
      //    .window(SlidingProcessingTimeWindows.of(Time.seconds(3),Time.seconds(2))
      //.window(ProcessingTimeSessionWindows.withGap(Time.seconds(3)))
    // 4.聚合
    val resultDS: DataStream[(String, Int)] = dataWS.sum(1)

    // 5.打印
    resultDS.print("time window")
  }
}
