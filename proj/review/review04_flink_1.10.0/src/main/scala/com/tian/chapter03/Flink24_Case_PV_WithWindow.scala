package com.tian.chapter03

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * @author tiankx
 * @date 2020/12/7 14:28
 * @version 1.0.0
 */
object Flink24_Case_PV_WithWindow {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val userBehaviorDS = env.readTextFile("files/user_behavior.txt")
    val result = userBehaviorDS
      .map(
        line => {
          val words = line.split("\\+")
          UserBehavior(
            words(0).toLong,
            words(1).toLong,
            words(2).toInt,
            words(3),
            words(4).toLong
          )
        }
      )
      .assignAscendingTimestamps(_.timestamp * 1000L)
      .filter(_.behavior == "pv")
      .map(_ => ("pv", 1))
      .keyBy(_._1)
      .timeWindow(Time.hours(1))
      .sum(1)
    result.print()
    env.execute("page view")
  }

  /**
   * 用户行为样例类
   *
   * @param userID     用户id
   * @param itemID     商品id
   * @param categoryID 商品类目id
   * @param behavior   用户行为类型
   * @param timestamp  时间戳（秒）
   */
  case class UserBehavior(userID: Long, itemID: Long, categoryID: Int, behavior: String, timestamp: Long)

}
