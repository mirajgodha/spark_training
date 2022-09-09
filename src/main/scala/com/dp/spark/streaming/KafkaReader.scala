package com.dp.spark.streaming

import java.sql.Timestamp

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{struct, to_json, _}
import org.apache.spark.sql.types.{StringType, _}


/**
 * To run the command run the following things:
 * sbt package
 * ../../spark-3.3.0-bin-hadoop3/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.0 --class com.dp.spark.streaming.KafkaReader target/scala-2.12/sparkscalacourse_2.12-0.1.jar
 *
 * Running kafka Consumer
 * bin/kafka-console-consumer.sh --topic test --from-beginning --bootstrap-server localhost:9092
 *
 * Running Kafka Producer
 *
 * bin/kafka-console-producer.sh --topic test --bootstrap-server localhost:9092
 *
 * First use the data/structured_streaming/streaming_data.json file on producer to show the things
 *
 * Read the data slowly from the data files and dump to kafa
 * perl -pe "system 'sleep .5'" /home/hadoop/code/spark_training/data/structured_streaming/part* | bin/kafka-console-producer.sh --topic test --bootstrap-server localhost:9092
 */
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

    println("By default we get all these columns with the stream............")
    kafkaInputDS.printSchema()

    val radioData = kafkaInputDS.select("radioCount")
    println("Just selecting our radioCount column............")
    radioData.printSchema

    radioData.createOrReplaceGlobalTempView("radio")

    val radioDataCols = spark.sql("select radioCount.title, " +
      "radioCount.artist," +
      "radioCount.radio," +
      "radioCount.count" +
      " from global_temp.radio")

    val counts = spark.sql("select sum(radioCount.count) , radioCount.artist from global_temp.radio group by radioCount.artist " )

    radioDataCols.writeStream
    .queryName("Debug Stream Kafka")
    .format("console")
    .start()

    counts.writeStream
      .queryName("Count of different artists")
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
