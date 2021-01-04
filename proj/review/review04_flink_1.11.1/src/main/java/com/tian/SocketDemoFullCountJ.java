package com.tian.chapter03;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/12/15 23:30
 */
public class SocketDemoFullCountJ {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketDS = env.socketTextStream("localhost", 9999, "\n");
        socketDS
                .map(new MapFunction<String, Tuple2<Integer, Integer>>() {
                    @Override
                    public Tuple2<Integer, Integer> map(String value) throws Exception {
                        return new Tuple2<>(1, Integer.parseInt(value));
                    }
                })
                .keyBy(new KeySelector<Tuple2<Integer, Integer>, Integer>() {
                    @Override
                    public Integer getKey(Tuple2<Integer, Integer> value) throws Exception {
                        return value.f0;
                    }
                })
                .timeWindow(Time.seconds(5))
                .process(new ProcessWindowFunction<Tuple2<Integer, Integer>, String, Integer, TimeWindow>() {
                    /**
                     *
                     * @param integer
                     * @param context
                     * @param elements
                     * @param out
                     */
                    @Override
                    public void process(
                            Integer integer,
                            Context context,
                            Iterable<Tuple2<Integer, Integer>> elements, Collector<String> out
                    ) {
                        System.out.println("execute process func ...");
                        long count = 0L;
                        for (Tuple2<Integer, Integer> element : elements)
                            count++;
                        out.collect("window:" + context.window() + ", count:" + count);
                    }
                })
                .print();
        env.execute("socket window count");
    }
}
