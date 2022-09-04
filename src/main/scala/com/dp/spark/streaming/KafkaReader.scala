package com.dp.spark.streaming

import java.sql.Timestamp

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{struct, to_json, _}
import org.apache.spark.sql.types.{StringType, _}


/** Read data from kafka topic */
object KafkaReader {


  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .appName("StructuredStreaming")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._

    val kafkaInputDS = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", KafkaService.topicName)
      .option("enable.auto.commit", false) // Kafka source doesn’t commit any offset.
      .option("group.id", "Structured-Streaming-Examples")
      .option("failOnDataLoss", false) // when starting a fresh kafka (default location is temporary (/tmp)
      .load()
      .withColumn(KafkaService.radioStructureName, // nested structure with our json
        from_json($"value".cast(StringType), KafkaService.schemaOutput) //From binary to JSON object
      ).as[SimpleSongAggregationKafka]

    kafkaInputDS
      .select(to_json(struct($"*")).cast(StringType).alias("value"))
      .writeStream
      .queryName("Debug Stream Kafka")
      .format("console")
      .start()

    //Wait for all streams to finish
    spark.streams.awaitAnyTermination()

    // Stop the session
    spark.stop()
  }

  case class SimpleSongAggregation(title: String, artist: String, radio: String, count: Long)

  case class SimpleSongAggregationKafka(topic: String, partition: Int, offset: Long, timestamp: Timestamp, radioCount: SimpleSongAggregation)

}
