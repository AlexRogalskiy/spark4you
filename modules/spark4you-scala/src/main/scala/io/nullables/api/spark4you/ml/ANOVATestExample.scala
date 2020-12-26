//// scalastyle:off println
//package io.nullables.api.spark4you.ml
//
//import org.apache.spark.ml.linalg.Vectors
//import org.apache.spark.sql.SparkSession
//
//// $example on$
//// $example off$
//
///**
// * An example for ANOVA testing.
// * Run with
// * {{{
// * bin/run-example ml.ANOVATestExample
// * }}}
// */
//object ANOVATestExample {
//
//    def main(args: Array[String]): Unit = {
//        val spark = SparkSession
//            .builder
//            .appName("ANOVATestExample")
//            .getOrCreate()
//
//        // $example on$
//        val data = Seq(
//            (3.0, Vectors.dense(1.7, 4.4, 7.6, 5.8, 9.6, 2.3)),
//            (2.0, Vectors.dense(8.8, 7.3, 5.7, 7.3, 2.2, 4.1)),
//            (3.0, Vectors.dense(1.2, 9.5, 2.5, 3.1, 8.7, 2.5)),
//            (2.0, Vectors.dense(3.7, 9.2, 6.1, 4.1, 7.5, 3.8)),
//            (4.0, Vectors.dense(8.9, 5.2, 7.8, 8.3, 5.2, 3.0)),
//            (4.0, Vectors.dense(7.9, 8.5, 9.2, 4.0, 9.4, 2.1))
//        )
//
//        val df = data.toDF("label", "features")
//        val anova = ANOVATest.test(df, "features", "label").head
//        println(s"pValues = ${anova.getAs[Vector](0)}")
//        println(s"degreesOfFreedom ${anova.getSeq[Int](1).mkString("[", ",", "]")}")
//        println(s"fValues ${anova.getAs[Vector](2)}")
//        // $example off$
//
//        spark.stop()
//    }
//}
//
//// scalastyle:on println
