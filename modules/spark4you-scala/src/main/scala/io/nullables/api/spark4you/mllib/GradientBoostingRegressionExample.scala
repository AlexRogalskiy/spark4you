// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.tree.GradientBoostedTrees
import org.apache.spark.mllib.tree.configuration.BoostingStrategy
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel
import org.apache.spark.mllib.util.MLUtils
// $example off$

object GradientBoostingRegressionExample {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("GradientBoostedTreesRegressionExample")
    val sc = new SparkContext(conf)
    // $example on$
    // Load and parse the data file.
    val data = MLUtils.loadLibSVMFile(sc, "data/mllib/sample_libsvm_data.txt")
    // Split the data into training and test sets (30% held out for testing)
    val splits = data.randomSplit(Array(0.7, 0.3))
    val (trainingData, testData) = (splits(0), splits(1))

    // Train a GradientBoostedTrees model.
    // The defaultParams for Regression use SquaredError by default.
    val boostingStrategy = BoostingStrategy.defaultParams("Regression")
    boostingStrategy.numIterations = 3 // Note: Use more iterations in practice.
    boostingStrategy.treeStrategy.maxDepth = 5
    // Empty categoricalFeaturesInfo indicates all features are continuous.
    boostingStrategy.treeStrategy.categoricalFeaturesInfo = Map[Int, Int]()

    val model = GradientBoostedTrees.train(trainingData, boostingStrategy)

    // Evaluate model on test instances and compute test error
    val labelsAndPredictions = testData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    val testMSE = labelsAndPredictions.map{ case(v, p) => math.pow((v - p), 2)}.mean()
    println(s"Test Mean Squared Error = $testMSE")
    println(s"Learned regression GBT model:\n ${model.toDebugString}")

    // Save and load model
    model.save(sc, "target/tmp/myGradientBoostingRegressionModel")
    val sameModel = GradientBoostedTreesModel.load(sc,
      "target/tmp/myGradientBoostingRegressionModel")
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println
