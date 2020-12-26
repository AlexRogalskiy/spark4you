// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.regression.{IsotonicRegression, IsotonicRegressionModel}
import org.apache.spark.mllib.util.MLUtils
// $example off$

object IsotonicRegressionExample {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("IsotonicRegressionExample")
    val sc = new SparkContext(conf)
    // $example on$
    val data = MLUtils.loadLibSVMFile(sc,
      "data/mllib/sample_isotonic_regression_libsvm_data.txt").cache()

    // Create label, feature, weight tuples from input data with weight set to default value 1.0.
    val parsedData = data.map { labeledPoint =>
      (labeledPoint.label, labeledPoint.features(0), 1.0)
    }

    // Split data into training (60%) and test (40%) sets.
    val splits = parsedData.randomSplit(Array(0.6, 0.4), seed = 11L)
    val training = splits(0)
    val test = splits(1)

    // Create isotonic regression model from training data.
    // Isotonic parameter defaults to true so it is only shown for demonstration
    val model = new IsotonicRegression().setIsotonic(true).run(training)

    // Create tuples of predicted and real labels.
    val predictionAndLabel = test.map { point =>
      val predictedLabel = model.predict(point._2)
      (predictedLabel, point._1)
    }

    // Calculate mean squared error between predicted and real labels.
    val meanSquaredError = predictionAndLabel.map { case (p, l) => math.pow((p - l), 2) }.mean()
    println(s"Mean Squared Error = $meanSquaredError")

    // Save and load model
    model.save(sc, "target/tmp/myIsotonicRegressionModel")
    val sameModel = IsotonicRegressionModel.load(sc, "target/tmp/myIsotonicRegressionModel")
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println
