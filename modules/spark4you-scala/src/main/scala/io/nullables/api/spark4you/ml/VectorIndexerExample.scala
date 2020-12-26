// scalastyle:off println
package io.nullables.api.spark4you.ml

// $example on$

import org.apache.spark.ml.feature.VectorIndexer
// $example off$
import org.apache.spark.sql.SparkSession

object VectorIndexerExample {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("VectorIndexerExample")
            .getOrCreate()

        // $example on$
        val data = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")

        val indexer = new VectorIndexer()
            .setInputCol("features")
            .setOutputCol("indexed")
            .setMaxCategories(10)

        val indexerModel = indexer.fit(data)

        val categoricalFeatures: Set[Int] = indexerModel.categoryMaps.keys.toSet
        println(s"Chose ${categoricalFeatures.size} " +
            s"categorical features: ${categoricalFeatures.mkString(", ")}")

        // Create new column "indexed" with categorical values transformed to indices
        val indexedData = indexerModel.transform(data)
        indexedData.show()
        // $example off$

        spark.stop()
    }
}

// scalastyle:on println
