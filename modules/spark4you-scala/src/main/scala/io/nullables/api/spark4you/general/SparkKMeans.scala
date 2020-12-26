package io.nullables.api.spark4you.general

import breeze.linalg.{DenseVector, Vector, squaredDistance}
import org.apache.spark.sql.SparkSession

/**
 * K-means clustering.
 *
 * This is an example implementation for learning how to use Spark. For more conventional use,
 * please refer to org.apache.spark.ml.clustering.KMeans.
 */
object SparkKMeans {

    def parseVector(line: String): Vector[Double] = {
        DenseVector(line.split(' ').map(_.toDouble))
    }

    def closestPoint(p: Vector[Double], centers: Array[Vector[Double]]): Int = {
        var bestIndex = 0
        var closest = Double.PositiveInfinity

        for (i <- centers.indices) {
            val tempDist = squaredDistance(p, centers(i))
            if (tempDist < closest) {
                closest = tempDist
                bestIndex = i
            }
        }

        bestIndex
    }

    def showWarning(): Unit = {
        System.err.println(
            """WARN: This is a naive implementation of KMeans Clustering and is given as an example!
              |Please use org.apache.spark.ml.clustering.KMeans
              |for more conventional use.
      """.stripMargin)
    }

    def main(args: Array[String]): Unit = {

        if (args.length < 3) {
            System.err.println("Usage: SparkKMeans <file> <k> <convergeDist>")
            System.exit(1)
        }

        showWarning()

        val spark = SparkSession
            .builder
            .appName("SparkKMeans")
            .getOrCreate()

        val lines = spark.read.textFile(args(0)).rdd
        val data = lines.map(parseVector _).cache()
        val K = args(1).toInt
        val convergeDist = args(2).toDouble

        val kPoints = data.takeSample(withReplacement = false, K, 42)
        var tempDist = 1.0

        while (tempDist > convergeDist) {
            val closest = data.map(p => (closestPoint(p, kPoints), (p, 1)))

            val pointStats = closest.reduceByKey(mergeResults)

            val newPoints = pointStats.map { pair =>
                (pair._1, pair._2._1 * (1.0 / pair._2._2))
            }.collectAsMap()

            tempDist = 0.0
            for (i <- 0 until K) {
                tempDist += squaredDistance(kPoints(i), newPoints(i))
            }

            for (newP <- newPoints) {
                kPoints(newP._1) = newP._2
            }
            println(s"Finished iteration (delta = $tempDist)")
        }

        println("Final centers:")
        kPoints.foreach(println)
        spark.stop()
    }

    private def mergeResults(
                                a: (Vector[Double], Int),
                                b: (Vector[Double], Int)): (Vector[Double], Int) = {
        (a._1 + b._1, a._2 + b._2)
    }
}
