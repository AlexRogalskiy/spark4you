// scalastyle:off println
package io.nullables.api.spark4you.ml

// $example on$

import java.util

import org.apache.spark.ml.attribute.{Attribute, AttributeGroup, NumericAttribute}
import org.apache.spark.ml.feature.VectorSlicer
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SparkSession}
// $example off$

object VectorSlicerExample {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("VectorSlicerExample")
            .getOrCreate()

        // $example on$
        val data = util.Arrays.asList(
            Row(Vectors.sparse(3, Seq((0, -2.0), (1, 2.3)))),
            Row(Vectors.dense(-2.0, 2.3, 0.0))
        )

        val defaultAttr = NumericAttribute.defaultAttr
        val attrs = Array("f1", "f2", "f3").map(defaultAttr.withName)
        val attrGroup = new AttributeGroup("userFeatures", attrs.asInstanceOf[Array[Attribute]])

        val dataset = spark.createDataFrame(data, StructType(Array(attrGroup.toStructField())))

        val slicer = new VectorSlicer().setInputCol("userFeatures").setOutputCol("features")

        slicer.setIndices(Array(1)).setNames(Array("f3"))
        // or slicer.setIndices(Array(1, 2)), or slicer.setNames(Array("f2", "f3"))

        val output = slicer.transform(dataset)
        output.show(false)
        // $example off$

        spark.stop()
    }
}

// scalastyle:on println
