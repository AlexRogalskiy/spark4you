package io.nullables.api.spark4you.mllib;// $example on$

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.IsotonicRegression;
import org.apache.spark.mllib.regression.IsotonicRegressionModel;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;

import scala.Tuple2;
import scala.Tuple3;

public class JavaIsotonicRegressionExample {
  public static void main(String[] args) {
    SparkConf sparkConf = new SparkConf().setAppName("JavaIsotonicRegressionExample");
    JavaSparkContext jsc = new JavaSparkContext(sparkConf);
    // $example on$
    JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(
      jsc.sc(), "data/mllib/sample_isotonic_regression_libsvm_data.txt").toJavaRDD();

    // Create label, feature, weight tuples from input data with weight set to default value 1.0.
    JavaRDD<Tuple3<Double, Double, Double>> parsedData = data.map(point ->
      new Tuple3<>(point.label(), point.features().apply(0), 1.0));

    // Split data into training (60%) and test (40%) sets.
    JavaRDD<Tuple3<Double, Double, Double>>[] splits =
      parsedData.randomSplit(new double[]{0.6, 0.4}, 11L);
    JavaRDD<Tuple3<Double, Double, Double>> training = splits[0];
    JavaRDD<Tuple3<Double, Double, Double>> test = splits[1];

    // Create isotonic regression model from training data.
    // Isotonic parameter defaults to true so it is only shown for demonstration
    IsotonicRegressionModel model = new IsotonicRegression().setIsotonic(true).run(training);

    // Create tuples of predicted and real labels.
    JavaPairRDD<Double, Double> predictionAndLabel = test.mapToPair(point ->
      new Tuple2<>(model.predict(point._2()), point._1()));

    // Calculate mean squared error between predicted and real labels.
    double meanSquaredError = predictionAndLabel.mapToDouble(pl -> {
      double diff = pl._1() - pl._2();
      return diff * diff;
    }).mean();
    System.out.println("Mean Squared Error = " + meanSquaredError);

    // Save and load model
    model.save(jsc.sc(), "target/tmp/myIsotonicRegressionModel");
    IsotonicRegressionModel sameModel =
      IsotonicRegressionModel.load(jsc.sc(), "target/tmp/myIsotonicRegressionModel");
    // $example off$

    jsc.stop();
  }
}
