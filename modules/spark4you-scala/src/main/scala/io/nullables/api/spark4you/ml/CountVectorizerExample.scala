// scalastyle:off println
package io.nullables.api.spark4you.ml

import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}
import org.apache.spark.sql.SparkSession

// $example on$
// $example off$

object CountVectorizerExample {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("CountVectorizerExample")
            .getOrCreate()

        // $example on$
        val df = spark.createDataFrame(Seq(
            (0, Array("a", "b", "c")),
            (1, Array("a", "b", "b", "c", "a"))
        )).toDF("id", "words")

        // fit a CountVectorizerModel from the corpus
        val cvModel: CountVectorizerModel = new CountVectorizer()
            .setInputCol("words")
            .setOutputCol("features")
            .setVocabSize(3)
            .setMinDF(2)
            .fit(df)

        // alternatively, define CountVectorizerModel with a-priori vocabulary
        val cvm = new CountVectorizerModel(Array("a", "b", "c"))
            .setInputCol("words")
            .setOutputCol("features")

        cvModel.transform(df).show(false)
        // $example off$

        spark.stop()
    }
}

// scalastyle:on println
