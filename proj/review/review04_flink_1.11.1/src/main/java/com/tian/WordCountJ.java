package com.tian;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/11/30 17:19
 */
public class WordCountJ {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> text = env.readTextFile("files/wordcount.txt");
        DataStream<Tuple2<String, Integer>> result = text
                .flatMap(new TokenizerJ())
                .keyBy(0)
                .sum(1);
        result.print().setParallelism(1);
        env.execute("word count java");
    }

    public static final class TokenizerJ implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] words = value.toLowerCase().split("\\W+");
            for (String word : words) {
                if (word.length() > 0) out.collect(new Tuple2<>(word, 1));
            }
        }
    }
}
