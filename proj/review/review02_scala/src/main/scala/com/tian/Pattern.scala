package com.tian


/**
 * @author tiankx
 *         2020/9/10 15:10
 * @version 1.0.0
 *
 *          模式匹配
 */
object Pattern {
  def main(args: Array[String]): Unit = {
    val arr = Array(10, 20, 30, 40, 50)
    arr match {
      case Array(0.2, 0.3, _*) => println("error")
      case _ => println("else")
      case Array(10, 20, 30, 40, 50) => println("same")
      case Array(_, _, _, _) => println("num")
    }

    val list = List(10, 20, 30, 40)
    list match {
      case List(_, a, _, _) => println(a)
      case List(a, _*) => println(a)
      case List(_, a@_*) => println(a)
      case a :: b :: c => println(c)
      case a :: b :: c :: Nil => println(c)
      case _ =>
    }

    val tuple = Tuple3(2, 3, 4)
    tuple match {
      case (1, 3, 4) => println(tuple)
      case (1, _, 4) => println("head and tail")
    }

    val f1 = fun1(_ + _)
    val f2 = fun1 {
      case (a, b) => a + b
    }
    println(f1 == f2)

    val list1 = List(1, 2, 3, "aa", false)
    val list2 = list1.map {
      case a: Int => a * a
      case _ =>
    }
    println(list2)
    val list3 = list1.collect {
      case a: Int => a * a
    }
    println(list3)
    val map1 = Map(1 -> (2, 3), 10 -> (20, 30), 100 -> (200, 300))
    val map2 = map1.map {
      case (a, (_, b)) => (a + 1, b + 1)
    }
    println(map2)
    //    (f1 _).apply(100)
    val f = f1 _
    //    f.apply(100)

  }

  def fun1(f: (Int, Int) => Int) = f(3, 4)

  def fun2(f: (((Int, Int), Int), Int) => Int) = f(((2, 3), 4), 5)

  def f0: PartialFunction[Int, Int] = {
    case a => 10
  }

  def f1(a: Int) = println(a)
}
