package com.tian

import org.apache.flink.api.common.functions.{FlatMapFunction, ReduceFunction}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.datastream.{DataStreamSource, SingleOutputStreamOperator}
import org.apache.flink.streaming.api.scala.{DataStream, KeySelectorWithType, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector


/**
 * @author tiankx
 * @date 2020/11/29 14:41
 * @version 1.0.0
 */
object SocketWindowWordCount {
  def main(args: Array[String]): Unit = {
    val hostname: String = "localhost"
    val port: Int = 9999
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val text: DataStream[String] = env.socketTextStream(hostname, port)
    val windowCounts: DataStream[WordWithCount] = text
      .flatMap(new FlatMapFunction[String, WordWithCount] {
        override def flatMap(value: String, out: Collector[WordWithCount]): Unit = {
          value.split("\\s").foreach((word: String) => out.collect(new WordWithCount(word, 1L)))
        }
      })

      .keyBy("word")
      .timeWindow(Time.seconds(5))
      .reduce(new ReduceFunction[WordWithCount] {
        override def reduce(t: WordWithCount, t1: WordWithCount): WordWithCount =
          WordWithCount(t.word, t.count + t1.count)
      })
    windowCounts.print().setParallelism(1) // 单线程输出
    env.execute("socket window wordcount")
  }

  case class WordWithCount(word: String, count: Long) {
    override def toString: String = word + ":" + count
  }

}
