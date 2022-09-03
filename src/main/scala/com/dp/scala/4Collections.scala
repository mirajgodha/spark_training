package com.dp.scala

object Collections {
  def main(args: Array[String]) {

    var scores = Map("scala" -> 10, "Spark" -> 8, "Hadoop" -> 6)
    println("Getting the Scala score from map: " + scores("scala"))

    var scores1 = Map(("scala", 10), ("Spark", 8), ("Hadoop", 6))
    println("Total of all the values: " + totalSeleVal(List(1, 2, 3, 4, 5), { e => true }))
    println("Total of only even numbers: " + totalSeleVal(List(1, 2, 3, 4, 5), { e => e % 2 == 0 }))
    println("Total of only numbers greater than 4: " + totalSeleVal(List(1, 2, 3, 4, 5), { _ > 4 }))

    println("----Now playing with list-----")
    val users: List[User1] = User1("joe", 22) :: User1("Bob", 43) :: User1("Kip", 56) :: Nil

  }


  /**
   * It does the total of selected value
   * Selector is been passed as an argument along with the function
   * @param list
   * @param selector
   * @return
   */
  def totalSeleVal(list: List[Int], selector: Int => Boolean) = {
    var sum = 0
    list foreach (e =>
      if (selector(e))
        sum += e)
    sum
  }

}

case class User1(name: String, age: Int)

