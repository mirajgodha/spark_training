package com.dp.scala

object ExceptionHandling {
  def main(args: Array[String]) {
    try {
      val x = 2 / 0
    } catch {
      case _: NumberFormatException => println("Use proper numbers")
      case ex: ArithmeticException => println("Check your math...  " + ex)
    }
  }
}

