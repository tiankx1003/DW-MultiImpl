package com.tian.chapter03

import com.tian.bean.HotItemClick
import com.tian.chapter03.Flink24_Case_PV_WithWindow.UserBehavior
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
 * 一小时内热门条目
 *
 * @author tiankx
 * @date 2020/12/7 14:51
 * @version 1.0.0
 */
object Flink26_Case_HotItemAnalysis {
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
      .keyBy(_.itemID)
      .timeWindow(Time.hours(1), Time.minutes(5))
      .aggregate( //传入预聚合函数预全窗口函数，实现累加逻辑
        /**
         * 预聚合函数，每接入一条数据就做一次聚合，知道触发窗口计算条件，把计算结果传给全窗口函数
         */
        new AggregateFunction[UserBehavior, Long, Long] {
          /** 定义累加器零值 */
          override def createAccumulator() = 0L

          /** 累加逻辑 */
          override def add(in: UserBehavior, acc: Long) = acc + 1L

          /** 获取结果值 */
          override def getResult(acc: Long) = acc

          /** 分区间累加？ */
          override def merge(acc: Long, acc1: Long) = acc + acc1
        },

        /**
         * 全窗口函数，为每个窗口的预聚合结果值打上窗口结束时间标记，用于后续区分来自不同窗口的数据
         */
        new ProcessWindowFunction[Long, HotItemClick, Long, TimeWindow] {
          override def process(key: Long, context: Context, elements: Iterable[Long], out: Collector[HotItemClick]): Unit =
            out.collect(HotItemClick(key, elements.iterator.next(), context.window.getEnd))
        }
      )
      .keyBy(_.windowEnd)
      .process(
        new KeyedProcessFunction[Long, HotItemClick, String] { // 有状态编程
          private var dataList: ListState[HotItemClick] = _
          private var triggerTS: ValueState[Long] = _

          // TODO: 2020-12-7 20:55:08 open方法为了实现什么？？？
          override def open(parameters: Configuration): Unit = {
            dataList = getRuntimeContext.getListState(new ListStateDescriptor[HotItemClick]("data list", classOf[HotItemClick]))
            triggerTS = getRuntimeContext.getState(new ValueStateDescriptor[Long]("trigger", classOf[Long]))
          }

          override def processElement(value: HotItemClick, ctx: KeyedProcessFunction[Long, HotItemClick, String]#Context, out: Collector[String]): Unit = {
            dataList.add(value)
            if (triggerTS.value() == 0) {
              ctx.timerService.registerEventTimeTimer(value.windowEnd)
              triggerTS.update(value.windowEnd)
            }
          }
        })
    result.print("hot item")
    env.execute()
  }

}
