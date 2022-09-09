package com.dp.spark.ml

// $example on$
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.clustering.KMeans
// $example off$
import org.apache.spark.sql.SparkSession


object KMeans {

  def main(args: Array[String]): Unit = {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName(s"${this.getClass.getSimpleName}")
      .getOrCreate()

    // Loads data.
    val dataset = spark.read.format("libsvm").load("data/sample_kmeans_data.txt")

    println("The raw data set passed ..")
    dataset.show()

    // Trains a k-means model.
    val kmeans = new KMeans().setK(3).setSeed(1L)
    val model = kmeans.fit(dataset)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
//    val WSSSE = model.computeCost(dataset)
//    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)
    println("clusterSizes: " + model.summary.clusterSizes.mkString(","))
    println("trainingCost: " + model.summary.trainingCost)
    println("featuresCol: " + model.summary.featuresCol)
    // $example off$

    spark.stop()
  }
}