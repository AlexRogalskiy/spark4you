// scalastyle:off println
package io.nullables.api.spark4you.mllib

import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.fpm.AssociationRules
import org.apache.spark.mllib.fpm.FPGrowth.FreqItemset
// $example off$

object AssociationRulesExample {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("AssociationRulesExample")
    val sc = new SparkContext(conf)

    // $example on$
    val freqItemsets = sc.parallelize(Seq(
      new FreqItemset(Array("a"), 15L),
      new FreqItemset(Array("b"), 35L),
      new FreqItemset(Array("a", "b"), 12L)
    ))

    val ar = new AssociationRules()
      .setMinConfidence(0.8)
    val results = ar.run(freqItemsets)

    results.collect().foreach { rule =>
    println(s"[${rule.antecedent.mkString(",")}=>${rule.consequent.mkString(",")} ]" +
        s" ${rule.confidence}")
    }
    // $example off$

    sc.stop()
  }

}
// scalastyle:on println
