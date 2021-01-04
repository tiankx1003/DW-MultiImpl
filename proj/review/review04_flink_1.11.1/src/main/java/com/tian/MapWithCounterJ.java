package com.tian;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.IOException;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/12/16 0:39
 */
public class MapWithCounterJ {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketDS = env.socketTextStream("localhost", 9999);
        socketDS
                .map((MapFunction<String, Tuple2<String, String>>) s -> new Tuple2<>("key", s))
                .map(new MapWithCounterFunctionJ())
                .print();
        env.execute();
    }
}

/**
 * 计算字符串长度之和
 */
class MapWithCounterFunctionJ extends RichMapFunction<Tuple2<String, String>, Long> {
    private ValueState<Long> totalLengthByKey;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ValueStateDescriptor<Long> desc
                = new ValueStateDescriptor<>("sum of length", LongSerializer.INSTANCE);
        totalLengthByKey = getRuntimeContext().getState(desc);
    }

    @Override
    public Long map(Tuple2<String, String> value) throws IOException {
        Long length = totalLengthByKey.value();
        if (length == null) length = 0L;
        long newLength = length + value.f1.length();
        totalLengthByKey.update(newLength);
        return newLength;
    }
}