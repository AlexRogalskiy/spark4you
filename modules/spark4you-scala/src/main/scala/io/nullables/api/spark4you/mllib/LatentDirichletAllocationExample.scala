// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.clustering.{DistributedLDAModel, LDA}
import org.apache.spark.mllib.linalg.Vectors
// $example off$

object LatentDirichletAllocationExample {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("LatentDirichletAllocationExample")
    val sc = new SparkContext(conf)

    // $example on$
    // Load and parse the data
    val data = sc.textFile("data/mllib/sample_lda_data.txt")
    val parsedData = data.map(s => Vectors.dense(s.trim.split(' ').map(_.toDouble)))
    // Index documents with unique IDs
    val corpus = parsedData.zipWithIndex.map(_.swap).cache()

    // Cluster the documents into three topics using LDA
    val ldaModel = new LDA().setK(3).run(corpus)

    // Output topics. Each is a distribution over words (matching word count vectors)
    println(s"Learned topics (as distributions over vocab of ${ldaModel.vocabSize} words):")
    val topics = ldaModel.topicsMatrix
    for (topic <- Range(0, 3)) {
      print(s"Topic $topic :")
      for (word <- Range(0, ldaModel.vocabSize)) {
        print(s"${topics(word, topic)}")
      }
      println()
    }

    // Save and load model.
    ldaModel.save(sc, "target/org/apache/spark/LatentDirichletAllocationExample/LDAModel")
    val sameModel = DistributedLDAModel.load(sc,
      "target/org/apache/spark/LatentDirichletAllocationExample/LDAModel")
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println
