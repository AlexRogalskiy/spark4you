// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.{SparkConf, SparkContext}

object StratifiedSamplingExample {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("StratifiedSamplingExample")
    val sc = new SparkContext(conf)

    // $example on$
    // an RDD[(K, V)] of any key value pairs
    val data = sc.parallelize(
      Seq((1, 'a'), (1, 'b'), (2, 'c'), (2, 'd'), (2, 'e'), (3, 'f')))

    // specify the exact fraction desired from each key
    val fractions = Map(1 -> 0.1, 2 -> 0.6, 3 -> 0.3)

    // Get an approximate sample from each stratum
    val approxSample = data.sampleByKey(withReplacement = false, fractions = fractions)
    // Get an exact sample from each stratum
    val exactSample = data.sampleByKeyExact(withReplacement = false, fractions = fractions)
    // $example off$

    println(s"approxSample size is ${approxSample.collect().size}")
    approxSample.collect().foreach(println)

    println(s"exactSample its size is ${exactSample.collect().size}")
    exactSample.collect().foreach(println)

    sc.stop()
  }
}
// scalastyle:on println
