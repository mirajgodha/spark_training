package com.dp.scala

/**
 * Passing function as variable also or define function as a variable
 * is also called "High order functions"
 *
 */
object Timer {

  /**
   *
   * @param callback is a name given to the function passed as a variable to this methoed
   *   () -> This empty braces means the function passed to this methord
   *   oncePerSecond does not requires any arguments
   *
   *   Unit -> Expains the return type of the function passed is null
   */
  def oncePerSecond(callback: () => Unit) {
    while (true) {
      callback()
      Thread sleep 1000
    } // 1-arg method sleep used as operator
  }

  /**
   * Welcome is a function which we will be passing as an argument
   */
  def welcome() {
    println("Welcome to Design Pathashala!")
  }

  /**
   * Main method
   * @param args
   */
  def main(args: Array[String]) {
    oncePerSecond(welcome)
  }
}

