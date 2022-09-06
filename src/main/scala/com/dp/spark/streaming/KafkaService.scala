package com.dp.spark.streaming

import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types._

object KafkaService {

  val radioStructureName = "radioCount"

  val topicName = "test"

  val bootstrapServers = "localhost:9092"

  val schemaOutput = new StructType()
    .add("title", StringType)
    .add("artist", StringType)
    .add("radio", StringType)
    .add("count", LongType)
}