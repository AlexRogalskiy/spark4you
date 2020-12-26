package io.nullables.api.spark4you.general

import org.apache.spark.sql.SparkSession

object ExceptionHandlingTest {
    
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("ExceptionHandlingTest")
            .getOrCreate()

        spark.sparkContext.parallelize(0 until spark.sparkContext.defaultParallelism).foreach { i =>
            if (math.random > 0.75) {
                throw new Exception("Testing exception handling")
            }
        }

        spark.stop()
    }
}
