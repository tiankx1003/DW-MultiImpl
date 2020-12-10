package com.tian.chapter03

import com.tian.chapter03.Flink24_Case_PV_WithWindow.UserBehavior
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable

/**
 * @author tiankx
 * @date 2020/12/7 14:35
 * @version 1.0.0
 */
object Flink25_Case_UV_WithWindow {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val textDS = env.readTextFile("files/user_behavior.txt")
    val result = textDS
      .map(
        line => {
          val words = line.split("\\W+")
          UserBehavior(
            words(0).toLong,
            words(1).toLong,
            words(2).toInt,
            words(3),
            words(4).toLong
          )
        }
      )
      .filter(_.behavior == "pv")
      .map(data => ("uv", data.userID))
      .keyBy(_._1)
      .timeWindow(Time.hours(1))
      .process(new ProcessWindowFunction[(String, Long), Long, String, TimeWindow] {
        val uvCount = mutable.Set[Long]()

        override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[Long]): Unit = {
          for (elem <- elements) uvCount.add(elem._2)
          out.collect(uvCount.size)
        }

        uvCount.clear()
      })

    result.print()
    env.execute("user view")
  }
}
