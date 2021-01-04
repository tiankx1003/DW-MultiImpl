package com.tian

import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
 * @author tiankx
 * @date 2020/12/15 23:59
 * @version 1.0.0
 */
object SocketDemoFullCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val socketDS = env.socketTextStream("localhost", 9999, '\n')
    socketDS
      .map(data => (1, data.toInt))
      .keyBy(new KeySelector[(Int, Int), Int] {
        override def getKey(in: (Int, Int)) = in._1
      })
      .timeWindow(Time.seconds(5))
      .process(new ProcessWindowFunction[(Int, Int), String, Int, TimeWindow] {
        override def process(key: Int, context: Context, elements: Iterable[(Int, Int)], out: Collector[String]): Unit = {
          println("execute process func ...")
          var count = 0L
          for (_ <- elements) count += 1
          out.collect("window:" + context.window + ",count:" + count)
        }
      })
      .print()
    env.execute("socket window count")
  }
}
