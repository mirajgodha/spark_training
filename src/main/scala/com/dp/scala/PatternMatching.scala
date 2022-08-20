package com.dp.scala

object PatternMatching {
  def main(args: Array[String]) {


    println("----Now playing with list-----")
    val users: List[User] = User("joe", 22) :: User("Bob", 43) :: User("Kip", 56) :: Nil

    println("Got the age " + getAge("Bob", users))
    println("---------")
    println("Got the age " + getAge("ABC", users))
  }


  /**
   * If given user is the first element in the list then user::tail is called
   * Otherwise head::tail is called, which again cal getAge function passing rest of the list as tail
   *
   * @param name
   * @param users
   * @return
   */
  def getAge(name: String, users: List[User]): Option[Int] = users match {
    case Nil => println("nil"); None
    case User(n, age) :: tail if n == name => println("user, tail " + n); Some(age)
    case head :: tail => println("head, tail"); getAge(name, tail)
  }
}

case class User(name: String, age: Int)

