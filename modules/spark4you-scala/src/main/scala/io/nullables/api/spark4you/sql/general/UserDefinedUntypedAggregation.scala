package io.nullables.api.spark4you.sql.general

import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{Encoder, Encoders, SparkSession, functions}

object UserDefinedUntypedAggregation {

    // $example on:untyped_custom_aggregation$
    case class Average(var sum: Long, var count: Long)

    object MyAverage extends Aggregator[Long, Average, Double] {
        // A zero value for this aggregation. Should satisfy the property that any b + zero = b
        def zero: Average = Average(0L, 0L)

        // Combine two values to produce a new value. For performance, the function may modify `buffer`
        // and return it instead of constructing a new object
        def reduce(buffer: Average, data: Long): Average = {
            buffer.sum += data
            buffer.count += 1
            buffer
        }

        // Merge two intermediate values
        def merge(b1: Average, b2: Average): Average = {
            b1.sum += b2.sum
            b1.count += b2.count
            b1
        }

        // Transform the output of the reduction
        def finish(reduction: Average): Double = reduction.sum.toDouble / reduction.count

        // Specifies the Encoder for the intermediate value type
        def bufferEncoder: Encoder[Average] = Encoders.product

        // Specifies the Encoder for the final output value type
        def outputEncoder: Encoder[Double] = Encoders.scalaDouble
    }

    // $example off:untyped_custom_aggregation$

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .appName("Spark SQL user-defined DataFrames aggregation example")
            .getOrCreate()

        // $example on:untyped_custom_aggregation$
        // Register the function to access it
        spark.udf.register("myAverage", functions.udaf(MyAverage))

        val df = spark.read.json("examples/src/main/resources/employees.json")
        df.createOrReplaceTempView("employees")
        df.show()
        // +-------+------+
        // |   name|salary|
        // +-------+------+
        // |Michael|  3000|
        // |   Andy|  4500|
        // | Justin|  3500|
        // |  Berta|  4000|
        // +-------+------+

        val result = spark.sql("SELECT myAverage(salary) as average_salary FROM employees")
        result.show()
        // +--------------+
        // |average_salary|
        // +--------------+
        // |        3750.0|
        // +--------------+
        // $example off:untyped_custom_aggregation$

        spark.stop()
    }
}
