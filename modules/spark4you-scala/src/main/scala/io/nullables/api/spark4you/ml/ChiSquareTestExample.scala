//// scalastyle:off println
//package io.nullables.api.spark4you.ml
//
//import org.apache.spark.ml.stat.ChiSquareTest
//import org.apache.spark.mllib.linalg.Vectors
//import org.apache.spark.sql.SparkSession
//
//// $example on$
//// $example off$
//
///**
// * An example for Chi-square hypothesis testing.
// * Run with
// * {{{
// * bin/run-example ml.ChiSquareTestExample
// * }}}
// */
//object ChiSquareTestExample {
//
//    def main(args: Array[String]): Unit = {
//        val spark = SparkSession
//            .builder
//            .appName("ChiSquareTestExample")
//            .getOrCreate()
//
//        // $example on$
//        val data = Seq(
//            (0.0, Vectors.dense(0.5, 10.0)),
//            (0.0, Vectors.dense(1.5, 20.0)),
//            (1.0, Vectors.dense(1.5, 30.0)),
//            (0.0, Vectors.dense(3.5, 30.0)),
//            (0.0, Vectors.dense(3.5, 40.0)),
//            (1.0, Vectors.dense(3.5, 40.0))
//        )
//
//        val df = data.toDF("label", "features")
//        val chi = ChiSquareTest.test(df, "features", "label").head
//        println(s"pValues = ${chi.getAs[Vector[String]](0)}")
//        println(s"degreesOfFreedom ${chi.getSeq[Int](1).mkString("[", ",", "]")}")
//        println(s"statistics ${chi.getAs[Vector[String]](2)}")
//        // $example off$
//
//        spark.stop()
//    }
//}
//
//// scalastyle:on println
