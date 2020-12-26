// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
// $example off$

object PMMLModelExportExample {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("PMMLModelExportExample")
    val sc = new SparkContext(conf)

    // $example on$
    // Load and parse the data
    val data = sc.textFile("data/mllib/kmeans_data.txt")
    val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()

    // Cluster the data into two classes using KMeans
    val numClusters = 2
    val numIterations = 20
    val clusters = KMeans.train(parsedData, numClusters, numIterations)

    // Export to PMML to a String in PMML format
    println(s"PMML Model:\n ${clusters.toPMML}")

    // Export the model to a local file in PMML format
    clusters.toPMML("/tmp/kmeans.xml")

    // Export the model to a directory on a distributed file system in PMML format
    clusters.toPMML(sc, "/tmp/kmeans")

    // Export the model to the OutputStream in PMML format
    clusters.toPMML(System.out)
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println
