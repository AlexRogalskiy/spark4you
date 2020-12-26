// package io.nullables.api.spark4you.ml;
//
// import java.util.Arrays;
// import java.util.List;
//
// import org.apache.spark.ml.feature.FValueSelector;
// import org.apache.spark.ml.linalg.VectorUDT;
// import org.apache.spark.ml.linalg.Vectors;
// import org.apache.spark.sql.Dataset;
// import org.apache.spark.sql.Row;
// import org.apache.spark.sql.RowFactory;
// import org.apache.spark.sql.SparkSession;
// import org.apache.spark.sql.types.*;
// // $example off$
//
// /**
//  * An example demonstrating FValueSelector.
//  * Run with
//  * <pre>
//  * bin/run-example ml.JavaFValueSelectorExample
//  * </pre>
//  */
// public class JavaFValueSelectorExample {
//   public static void main(String[] args) {
//     SparkSession spark = SparkSession
//       .builder()
//       .appName("JavaFValueSelectorExample")
//       .getOrCreate();
//
//     // $example on$
//     List<Row> data = Arrays.asList(
//       RowFactory.create(1, Vectors.dense(6.0, 7.0, 0.0, 7.0, 6.0, 0.0), 4.6),
//       RowFactory.create(2, Vectors.dense(0.0, 9.0, 6.0, 0.0, 5.0, 9.0), 6.6),
//       RowFactory.create(3, Vectors.dense(0.0, 9.0, 3.0, 0.0, 5.0, 5.0), 5.1),
//       RowFactory.create(4, Vectors.dense(0.0, 9.0, 8.0, 5.0, 6.0, 4.0), 7.6),
//       RowFactory.create(5, Vectors.dense(8.0, 9.0, 6.0, 5.0, 4.0, 4.0), 9.0),
//       RowFactory.create(6, Vectors.dense(8.0, 9.0, 6.0, 4.0, 0.0, 0.0), 9.0)
//     );
//     StructType schema = new StructType(new StructField[]{
//       new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
//       new StructField("features", new VectorUDT(), false, Metadata.empty()),
//       new StructField("label", DataTypes.DoubleType, false, Metadata.empty())
//     });
//
//     Dataset<Row> df = spark.createDataFrame(data, schema);
//
//     FValueSelector selector = new FValueSelector()
//       .setNumTopFeatures(1)
//       .setFeaturesCol("features")
//       .setLabelCol("label")
//       .setOutputCol("selectedFeatures");
//
//     Dataset<Row> result = selector.fit(df).transform(df);
//
//     System.out.println("FValueSelector output with top " + selector.getNumTopFeatures()
//         + " features selected");
//     result.show();
//
//     // $example off$
//     spark.stop();
//   }
// }
