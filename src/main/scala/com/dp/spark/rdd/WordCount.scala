package com.dp.spark.rdd

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

/** Count up how many of each word appears in a book as simply as possible. */
object WordCount {

  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "WordCount")

    // Read each line of my book into an RDD
    val input = sc.textFile("data/book.txt")

    // Split into words separated by a space character
    val words = input.flatMap(x => x.split(" "))

    // Count up the occurrences of each word
    //This method should only be used if the resulting map is expected to be small, as
    //the whole thing is loaded into the driver's memory.
    val wordCounts = words.countByValue()

    // Print the results.
    wordCounts.foreach(println)
  }

}
