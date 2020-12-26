//// scalastyle:off println
//package io.nullables.api.spark4you.ml
//
//// $example on$
//
//import org.apache.spark.ml.linalg.Vectors
//// $example off$
//import org.apache.spark.sql.SparkSession
//
//// $example on$
//// $example off$
//
///**
// * An example for ANOVASelector.
// * Run with
// * {{{
// * bin/run-example ml.ANOVASelectorExample
// * }}}
// */
//object ANOVASelectorExample {
//
//    def main(args: Array[String]): Unit = {
//        val spark = SparkSession
//            .builder
//            .appName("ANOVASelectorExample")
//            .getOrCreate()
//
//        // $example on$
//        val data = Seq(
//            (1, Vectors.dense(1.7, 4.4, 7.6, 5.8, 9.6, 2.3), 3.0),
//            (2, Vectors.dense(8.8, 7.3, 5.7, 7.3, 2.2, 4.1), 2.0),
//            (3, Vectors.dense(1.2, 9.5, 2.5, 3.1, 8.7, 2.5), 3.0),
//            (4, Vectors.dense(3.7, 9.2, 6.1, 4.1, 7.5, 3.8), 2.0),
//            (5, Vectors.dense(8.9, 5.2, 7.8, 8.3, 5.2, 3.0), 4.0),
//            (6, Vectors.dense(7.9, 8.5, 9.2, 4.0, 9.4, 2.1), 4.0)
//        )
//
//        val df = spark.createDataset(data).toDF("id", "features", "label")
//
//        val selector = new ANOVASelector()
//            .setNumTopFeatures(1)
//            .setFeaturesCol("features")
//            .setLabelCol("label")
//            .setOutputCol("selectedFeatures")
//
//        val result = selector.fit(df).transform(df)
//
//        println(s"ANOVASelector output with top ${selector.getNumTopFeatures} features selected")
//        result.show()
//        // $example off$
//
//        spark.stop()
//    }
//}
//
//// scalastyle:on println
