package com.tian;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/11/29 16:00
 */
public class SocketWindowWordCountJ {
    public static void main(String[] args) throws Exception {
        final String hostname = "localhost";
        final int port = 9999;
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // get input data by connecting to the socket
        DataStream<String> text = env.socketTextStream(hostname, port, "\n");

        // parse the data, group it, window it, and aggregate the counts
        DataStream<WordWithCountJ> windowCounts = text

                .flatMap(new FlatMapFunction<String, WordWithCountJ>() {
                    @Override
                    public void flatMap(String value, Collector<WordWithCountJ> out) {
                        for (String word : value.split("\\s")) {
                            out.collect(new WordWithCountJ(word, 1L));
                        }
                    }
                })

                .keyBy(new KeySelector<WordWithCountJ, Object>() {
                    @Override
                    public Object getKey(WordWithCountJ wordWithCountJ) throws Exception {
                        return wordWithCountJ.word;
                    }
                })
                .timeWindow(Time.seconds(5))
                .reduce(new ReduceFunction<WordWithCountJ>() {
                    @Override
                    public WordWithCountJ reduce(WordWithCountJ a, WordWithCountJ b) {
                        return new WordWithCountJ(a.word, a.count + b.count);
                    }
                });

        // print the results with a single thread, rather than in parallel
        windowCounts.print().setParallelism(1);

        env.execute("Socket Window WordCount");

    }

    /**
     * @author tiankx
     * @version 1.0.0
     * @date 2020/11/29 16:03
     */
    public static class WordWithCountJ {
        public String word;
        public long count;

        public WordWithCountJ(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + ":" + count;
        }
    }
}
