package com.tian;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/12/16 9:01
 */
public class MapWithCheckPointedFunctionJ { // TODO 2020-12-16 18:55:01 没看懂, 泛型擦除
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketDS = env.socketTextStream("localhost", 9999);
        socketDS
                .map(new MapFunction<String, Tuple2<String, String>>() { // TODO 2020-12-16 19:41:37 此处使用lambda表达式会因为泛型擦除报错
                    @Override
                    public Tuple2<String, String> map(String s) {
                        return new Tuple2<>(s, "map");
                    }
                })
                .keyBy((KeySelector<Tuple2<String, String>, String>) s -> s.f0)
                .map(new CustomMapFunctionJ<>())
                .print();
        socketDS.print();
        env.execute();
    }

    static class CustomMapFunctionJ<T> implements MapFunction<T, T>, CheckpointedFunction {
        private ReducingState<Long> countPerKey;
        private ListState<Long> countPerPartition;
        private long localCount;

        @Override
        public T map(T t) throws Exception {
            countPerKey.add(1L);
            localCount++;
            return t;
        }

        @Override
        public void snapshotState(FunctionSnapshotContext context) throws Exception {
            countPerPartition.clear();
            countPerPartition.add(localCount);
        }

        @Override
        public void initializeState(FunctionInitializationContext context) throws Exception {
            countPerKey = context.getKeyedStateStore().getReducingState(
                    new ReducingStateDescriptor<>("perKeyCount", new AddFunctionJ(), Long.class)
            );
            countPerPartition = context.getOperatorStateStore().getUnionListState(
                    new ListStateDescriptor<>("perPartitionCount", Long.class)
            );
            for (Long a : countPerPartition.get()) {
                localCount += a;
            }
        }
    }

    static class AddFunctionJ implements ReduceFunction<Long> {

        @Override
        public Long reduce(Long t, Long t1) {
            return t + t1;
        }
    }
}
