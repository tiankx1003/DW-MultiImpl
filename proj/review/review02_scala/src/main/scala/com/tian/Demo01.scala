package com.tian

/**
 * @author tiankx
 * @date 2020/9/9 12:15
 * @version 1.0.0
 *
 *
 */
object Demo01 {
    def main(args: Array[String]): Unit = {
        val list1 = List("Hello", "hadoop test", "test tian", "world hello")
        val list2 = list1.flatMap(_.split("\\W+"))
        val list3 = list1.map(_.split("\\W+").toList)
        val list4 = list1.flatMap(x => List())

    }
}

sealed abstract class SuperClass

object Aa extends SuperClass // 通过继承限制类实现单例

object Bb //
