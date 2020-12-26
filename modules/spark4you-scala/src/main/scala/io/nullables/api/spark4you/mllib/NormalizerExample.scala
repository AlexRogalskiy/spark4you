// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
// $example on$
import org.apache.spark.mllib.feature.Normalizer
import org.apache.spark.mllib.util.MLUtils
// $example off$

object NormalizerExample {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("NormalizerExample")
    val sc = new SparkContext(conf)

    // $example on$
    val data = MLUtils.loadLibSVMFile(sc, "data/mllib/sample_libsvm_data.txt")

    val normalizer1 = new Normalizer()
    val normalizer2 = new Normalizer(p = Double.PositiveInfinity)

    // Each sample in data1 will be normalized using $L^2$ norm.
    val data1 = data.map(x => (x.label, normalizer1.transform(x.features)))

    // Each sample in data2 will be normalized using $L^\infty$ norm.
    val data2 = data.map(x => (x.label, normalizer2.transform(x.features)))
    // $example off$

    println("data1: ")
    data1.collect.foreach(x => println(x))

    println("data2: ")
    data2.collect.foreach(x => println(x))

    sc.stop()
  }
}
// scalastyle:on println
