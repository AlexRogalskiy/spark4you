// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.random.RandomRDDs
import org.apache.spark.rdd.RDD

/**
 * An example app for randomly generated RDDs. Run with
 * {{{
 * bin/run-example org.apache.spark.examples.mllib.RandomRDDGeneration
 * }}}
 * If you use it as a template to create your own app, please use `spark-submit` to submit your app.
 */
object RandomRDDGeneration {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName(s"RandomRDDGeneration")
    val sc = new SparkContext(conf)

    val numExamples = 10000 // number of examples to generate
    val fraction = 0.1 // fraction of data to sample

    // Example: RandomRDDs.normalRDD
    val normalRDD: RDD[Double] = RandomRDDs.normalRDD(sc, numExamples)
    println(s"Generated RDD of ${normalRDD.count()}" +
      " examples sampled from the standard normal distribution")
    println("  First 5 samples:")
    normalRDD.take(5).foreach( x => println(s"    $x") )

    // Example: RandomRDDs.normalVectorRDD
    val normalVectorRDD = RandomRDDs.normalVectorRDD(sc, numRows = numExamples, numCols = 2)
    println(s"Generated RDD of ${normalVectorRDD.count()} examples of length-2 vectors.")
    println("  First 5 samples:")
    normalVectorRDD.take(5).foreach( x => println(s"    $x") )

    println()

    sc.stop()
  }

}
// scalastyle:on println
