package io.nullables.api.spark4you.ml;

import static org.apache.spark.sql.types.DataTypes.*;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
// $example off$

public class JavaStringIndexerExample {
  public static void main(String[] args) {
    SparkSession spark = SparkSession
      .builder()
      .appName("JavaStringIndexerExample")
      .getOrCreate();

    // $example on$
    List<Row> data = Arrays.asList(
      RowFactory.create(0, "a"),
      RowFactory.create(1, "b"),
      RowFactory.create(2, "c"),
      RowFactory.create(3, "a"),
      RowFactory.create(4, "a"),
      RowFactory.create(5, "c")
    );
    StructType schema = new StructType(new StructField[]{
      createStructField("id", IntegerType, false),
      createStructField("category", StringType, false)
    });
    Dataset<Row> df = spark.createDataFrame(data, schema);

    StringIndexer indexer = new StringIndexer()
      .setInputCol("category")
      .setOutputCol("categoryIndex");

    Dataset<Row> indexed = indexer.fit(df).transform(df);
    indexed.show();
    // $example off$

    spark.stop();
  }
}
