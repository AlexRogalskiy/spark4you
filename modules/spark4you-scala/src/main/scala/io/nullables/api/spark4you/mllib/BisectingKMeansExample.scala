package io.nullables.api.spark4you.mllib

// scalastyle:off println
import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.clustering.BisectingKMeans
import org.apache.spark.mllib.linalg.{Vector, Vectors}
// $example off$

/**
 * An example demonstrating a bisecting k-means clustering in spark.mllib.
 *
 * Run with
 * {{{
 * bin/run-example mllib.BisectingKMeansExample
 * }}}
 */
object BisectingKMeansExample {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("mllib.BisectingKMeansExample")
    val sc = new SparkContext(sparkConf)

    // $example on$
    // Loads and parses data
    def parse(line: String): Vector = Vectors.dense(line.split(" ").map(_.toDouble))
    val data = sc.textFile("data/mllib/kmeans_data.txt").map(parse).cache()

    // Clustering the data into 6 clusters by BisectingKMeans.
    val bkm = new BisectingKMeans().setK(6)
    val model = bkm.run(data)

    // Show the compute cost and the cluster centers
    println(s"Compute Cost: ${model.computeCost(data)}")
    model.clusterCenters.zipWithIndex.foreach { case (center, idx) =>
      println(s"Cluster Center ${idx}: ${center}")
    }
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println
