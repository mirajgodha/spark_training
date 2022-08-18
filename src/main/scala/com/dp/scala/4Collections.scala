package com.dp.scala

object Basics {
  def main(args: Array[String]) {
    try {
      val x = 2 / 0
    } catch {
      case _: NumberFormatException => println("Use proper numbers")
      case ex: ArithmeticException  => println("Check your math...  " + ex)
    }

    var scores = Map("scala" -> 10, "Spark" -> 8, "Hadoop" -> 6)
    println("Getting the Scala score from map: " + scores("scala"))

    var scores1 = Map(("scala", 10), ("Spark", 8), ("Hadoop", 6))
    println("Total of all the values: " + totalSeleVal(List(1, 2, 3, 4, 5), { e => true }))
    println("Total of only even numbers: " + totalSeleVal(List(1, 2, 3, 4, 5), { e => e % 2 == 0 }))
    println("Total of only numbers greater than 4: " + totalSeleVal(List(1, 2, 3, 4, 5), { _ > 4 }))

    println("----Now playing with list-----")
    val users: List[User] = User("joe", 22) :: User("Bob", 43) :: User("Kip", 56) :: Nil

    println("Got the age " +getAge("Bob", users))
    println("---------")
    println("Got the age " +getAge("ABC", users))
  }

  /**
   * If given user is the first element in the list then user::tail is called
   * Otherwise head::tail is called, which again cal getAge function passing rest of the list as tail
   * @param name
   * @param users
   * @return
   */
  def getAge(name: String, users: List[User]): Option[Int] = users match {
    case Nil                               => println("nil") ;None
    case User(n, age) :: tail if n == name => println("user, tail " + n) ;Some(age)
    case head :: tail                      => println("head, tail") ; getAge(name, tail)
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

case class User(name: String, age: Int)

