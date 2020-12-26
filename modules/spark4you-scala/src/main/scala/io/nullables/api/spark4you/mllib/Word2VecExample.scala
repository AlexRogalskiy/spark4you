// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
// $example on$
import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}
// $example off$

object Word2VecExample {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Word2VecExample")
    val sc = new SparkContext(conf)

    // $example on$
    val input = sc.textFile("data/mllib/sample_lda_data.txt").map(line => line.split(" ").toSeq)

    val word2vec = new Word2Vec()

    val model = word2vec.fit(input)

    val synonyms = model.findSynonyms("1", 5)

    for((synonym, cosineSimilarity) <- synonyms) {
      println(s"$synonym $cosineSimilarity")
    }

    // Save and load model
    model.save(sc, "myModelPath")
    val sameModel = Word2VecModel.load(sc, "myModelPath")
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println
