package com.dp.scala

trait Ord {
  def < (that: Any): Boolean
  def <=(that: Any): Boolean =  (this < that) || (this == that)
  def > (that: Any): Boolean = !(this <= that)
  def >=(that: Any): Boolean = !(this < that)
}

class Date(y: Int, m: Int, d: Int) extends Ord {
  def year  = y
  def month = m
  def day   = d
  override def toString(): String = s"$year-$month-$day"

  override def equals(that: Any): Boolean =
    that.isInstanceOf[Date] && {
      val o = that.asInstanceOf[Date]
      o.day == day && o.month == month && o.year == year
    }

  def <(that: Any): Boolean = {
    if (!that.isInstanceOf[Date])
      sys.error(s"Cannot compare $that and a Date")
    val o = that.asInstanceOf[Date]
    (year < o.year) ||
      (year == o.year && (month < o.month ||
        (month == o.month && day < o.day)))
  }
}

object OrderedDateTest {
  def main(args: Array[String]) {
    val d1 = new Date(2000,1,1)
    val d2 = new Date(2019,5,11)
    println(s"d1:        $d1")
    println(s"d2:        $d2")
    println(s"d1 == d1:  ${d1 == d1 == true}")
    println(s"d1 != d1:  ${d1 != d1 == false}")
    println(s"d2 == d2:  ${d2 == d2 == true}")
    println(s"d2 != d2:  ${d2 != d2 == false}")
    println(s"d1 <  d1:  ${d1 <  d1 == false}")
    println(s"d1 <= d1:  ${d1 <= d1 == true}")
    println(s"d1 >  d1:  ${d1 >  d1 == false}")
    println(s"d1 >= d1:  ${d1 >= d1 == true}")
    println(s"d1 == d2:  ${d1 == d2 == false}")
    println(s"d1 != d2:  ${d1 != d2 == true}")
    println(s"d1 <  d2:  ${d1 <  d2 == true}")
    println(s"d1 <= d2:  ${d1 <= d2 == true}")
    println(s"d1 >  d2:  ${d1 >  d2 == false}")
    println(s"d1 >= d2:  ${d2 >= d1 == true}")
    println(s"d2 != d1:  ${d2 != d1 == true}")
    println(s"d2 <  d1:  ${d2 <  d1 == false}")
    println(s"d2 <= d1:  ${d2 <= d1 == false}")
    println(s"d2 >  d1:  ${d2 >  d1 == true}")
    println(s"d2 >= d1:  ${d2 >= d1 == true}")
    // println(s"d2 >= 13:  ${d2 >= 13 == false}") // causes error
  }

}