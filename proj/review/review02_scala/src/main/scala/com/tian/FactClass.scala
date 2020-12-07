package com.tian

import scala.collection.mutable

/**
 * @author tiankx
 * @date 2020/9/9 13:32
 * @version 1.0.0
 *
 * 通过伴生实现工厂类
 */
object FactClass {
    def main(args: Array[String]): Unit = {
        println(FactClass.getFact("red"))
        println(FactClass.getFact("yellow"))
        println(FactClass.getFact("blue"))
    }

    val factClass: mutable.Map[String, FactClass] = mutable.Map( // 可变map
        "red" -> new FactClass("red"),
        "blue" -> new FactClass("blue"),
        "yellow" -> new FactClass("yellow")
    )

    def getFact(color: String): FactClass =
        factClass.getOrElseUpdate(color, new FactClass(color))
}


class FactClass private(val color: String) {
    println(s"$color Factory")
    override def toString: String = s"$color"
}