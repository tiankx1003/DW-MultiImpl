package com.tian;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
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
 * @date 2020/12/16 0:08
 */
public class CountWindowAverageDemoJ {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketDS = env.socketTextStream("localhost", 9999);
        socketDS
                .map((MapFunction<String, Tuple2<String, Integer>>) s -> new Tuple2<>(s, 1))
                .keyBy((KeySelector<Tuple2<String, Integer>, String>) t -> t.f0)
                .timeWindow(Time.seconds(5))
                .process(new ProcessWindowFunction<Tuple2<String, Integer>, String, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<Tuple2<String, Integer>> elements, Collector<String> out) {
                        int count = 0;
                        int sum = 0;
                        for (Tuple2<String, Integer> element : elements) {
                            count++;
                            sum += Integer.parseInt(element.f0);
                        }
                        out.collect("avg:" + sum / count);
                    }
                })
                .print();
        env.execute("window avg");
    }
}
