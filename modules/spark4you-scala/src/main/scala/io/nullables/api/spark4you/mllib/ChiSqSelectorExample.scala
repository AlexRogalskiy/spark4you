// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
// $example on$
import org.apache.spark.mllib.feature.ChiSqSelector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
// $example off$

object ChiSqSelectorExample {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("ChiSqSelectorExample")
    val sc = new SparkContext(conf)

    // $example on$
    // Load some data in libsvm format
    val data = MLUtils.loadLibSVMFile(sc, "data/mllib/sample_libsvm_data.txt")
    // Discretize data in 16 equal bins since ChiSqSelector requires categorical features
    // Even though features are doubles, the ChiSqSelector treats each unique value as a category
    val discretizedData = data.map { lp =>
      LabeledPoint(lp.label, Vectors.dense(lp.features.toArray.map { x => (x / 16).floor }))
    }
    // Create ChiSqSelector that will select top 50 of 692 features
    val selector = new ChiSqSelector(50)
    // Create ChiSqSelector model (selecting features)
    val transformer = selector.fit(discretizedData)
    // Filter the top 50 features from each feature vector
    val filteredData = discretizedData.map { lp =>
      LabeledPoint(lp.label, transformer.transform(lp.features))
    }
    // $example off$

    println("filtered data: ")
    filteredData.collect.foreach(x => println(x))

    sc.stop()
  }
}
// scalastyle:on println
