//// scalastyle:off println
//package io.nullables.api.spark4you.ml
//
//import java.io.File
//
//import org.apache.spark.ml.linalg.Vector
//import org.apache.spark.mllib.linalg.Vectors
//import org.apache.spark.mllib.stat.MultivariateOnlineSummarizer
//import org.apache.spark.sql.{DataFrame, Row, SparkSession}
//import org.apache.spark.util.Utils.createTempDir
//import scopt.OptionParser
//
///**
// * An example of how to use [[DataFrame]] for ML. Run with
// * {{{
// * ./bin/run-example ml.DataFrameExample [options]
// * }}}
// * If you use it as a template to create your own app, please use `spark-submit` to submit your app.
// */
//object DataFrameExample {
//
//    case class Params(input: String = "data/mllib/sample_libsvm_data.txt")
//        extends AbstractParams[Params]
//
//    def main(args: Array[String]): Unit = {
//        val defaultParams = Params()
//
//        val parser = new OptionParser[Params]("DataFrameExample") {
//            head("DataFrameExample: an example app using DataFrame for ML.")
//            opt[String]("input")
//                .text("input path to dataframe")
//                .action((x, c) => c.copy(input = x))
//            checkConfig { params =>
//                success
//            }
//        }
//
//        parser.parse(args, defaultParams) match {
//            case Some(params) => run(params)
//            case _ => sys.exit(1)
//        }
//    }
//
//    def run(params: Params): Unit = {
//        val spark = SparkSession
//            .builder
//            .appName(s"DataFrameExample with $params")
//            .getOrCreate()
//
//        // Load input data
//        println(s"Loading LIBSVM file with UDT from ${params.input}.")
//        val df: DataFrame = spark.read.format("libsvm").load(params.input).cache()
//        println("Schema from LIBSVM:")
//        df.printSchema()
//        println(s"Loaded training data as a DataFrame with ${df.count()} records.")
//
//        // Show statistical summary of labels.
//        val labelSummary = df.describe("label")
//        labelSummary.show()
//
//        // Convert features column to an RDD of vectors.
//        val features = df.select("features").rdd.map { case Row(v: Vector) => v }
//        val featureSummary = features.aggregate(new MultivariateOnlineSummarizer())(
//            (summary, feat) => summary.add(Vectors.fromML(feat)),
//            (sum1, sum2) => sum1.merge(sum2))
//        println(s"Selected features column with average values:\n ${featureSummary.mean.toString}")
//
//        // Save the records in a parquet file.
//        val tmpDir = createTempDir()
//        val outputDir = new File(tmpDir, "dataframe").toString
//        println(s"Saving to $outputDir as Parquet file.")
//        df.write.parquet(outputDir)
//
//        // Load the records back.
//        println(s"Loading Parquet file with UDT from $outputDir.")
//        val newDF = spark.read.parquet(outputDir)
//        println("Schema from Parquet:")
//        newDF.printSchema()
//
//        spark.stop()
//    }
//}
//
//// scalastyle:on println
