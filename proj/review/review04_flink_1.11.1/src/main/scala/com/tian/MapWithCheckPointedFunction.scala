package com.tian

import org.apache.flink.api.common.functions.{MapFunction, ReduceFunction}
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, ReducingState, ReducingStateDescriptor}
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * @author tiankx
 * @date 2020/12/16 19:44
 * @version 1.0.0
 */
object MapWithCheckPointedFunction {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val socketDS = env.socketTextStream("local", 9999)


  }

  class CustomMapFunction[T] extends MapFunction[T, T] with CheckpointedFunction {
    var perKeyCount: ReducingState[Long] = _
    var perPartitionCount: ListState[Long] = _
    var localCount: Long = 0L

    override def map(t: T) = {
      perKeyCount.add(1L)
      localCount += 1
      t
    }

    override def snapshotState(context: FunctionSnapshotContext) = ???

    override def initializeState(context: FunctionInitializationContext) = {
      perKeyCount = context.getKeyedStateStore.getReducingState(new ReducingStateDescriptor("perKeyCount", new AddFunction(), classOf[Long]))
      perPartitionCount = context.getOperatorStateStore.getListState(new ListStateDescriptor[Long]("perPartitionCount", classOf[Long]))
      //perPartitionCount.get().forEach
    }
  }

  class AddFunction extends ReduceFunction[Long] {
    override def reduce(t: Long, t1: Long) = t + t1
  }

}
