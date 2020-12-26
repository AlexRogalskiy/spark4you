package io.nullables.api.spark4you.streaming;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import io.nullables.api.spark4you.streaming.general.StreamingExamples;
import scala.Tuple2;

public final class JavaQueueStream {

    public static void main(final String[] args) throws Exception {
        StreamingExamples.setStreamingLogLevels();
        SparkConf sparkConf = new SparkConf().setAppName("JavaQueueStream");

        // Create the context
        JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, new Duration(1000));

        // Create the queue through which RDDs can be pushed to
        // a QueueInputDStream

        // Create and push some RDDs into the queue
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        Queue<JavaRDD<Integer>> rddQueue = new LinkedList<>();
        for (int i = 0; i < 30; i++) {
            rddQueue.add(ssc.sparkContext().parallelize(list));
        }

        // Create the QueueInputDStream and use it do some processing
        JavaDStream<Integer> inputStream = ssc.queueStream(rddQueue);
        JavaPairDStream<Integer, Integer> mappedStream = inputStream.mapToPair(
            (PairFunction<Integer, Integer, Integer>) s -> new Tuple2<Integer, Integer>(s % 10, 1));
        JavaPairDStream<Integer, Integer> reducedStream = mappedStream.reduceByKey(Integer::sum);

        reducedStream.print();
        ssc.start();
        ssc.awaitTermination();
    }
}
