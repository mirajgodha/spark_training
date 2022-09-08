package com.dp.spark.broadcast

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.broadcast

import scala.io.{Codec, Source}

/** Find the movies with the most ratings. */
object Broadcast {
  case class Employee(name:String, age:Int, depId: String)
  case class Department(id: String, name: String)
  /** Our main function where the action happens */
  def main(args: Array[String]) {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
//    val sc = new SparkContext("local[*]", "Broadcast")
    // Create a SparkSession using every core of the local machine
    val spark = SparkSession
      .builder
      .appName("Broadcast")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._


    val employeesRDD = spark.sparkContext.parallelize(Seq(
      Employee("Mary", 33, "IT"),
      Employee("Paul", 45, "IT"),
      Employee("Peter", 26, "MKT"),
      Employee("Jon", 34, "MKT"),
      Employee("Sarah", 29, "IT"),
      Employee("Steve", 21, "Intern")
    ))
    val departmentsRDD = spark.sparkContext.parallelize(Seq(
      Department("IT", "IT  Department"),
      Department("MKT", "Marketing Department"),
      Department("FIN", "Finance & Controlling")
    ))

    val employeesDF = employeesRDD.toDF
    val departmentsDF = departmentsRDD.toDF
    // The DataFrame API of Spark makes it very concise to create a broadcast variable out of the department DataFrame.


    // materializing the department data
    val tmpDepartments = broadcast(departmentsDF.as("departments"))

    // join by employees.depID == departments.id
    employeesDF.join(tmpDepartments,
      $"depId" === $"id",
      "inner").show()
  }

}
