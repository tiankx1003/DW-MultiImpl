package com.tian

import java.io.File

import scala.io.Source

/**
 * @author tiankx
 *         2020/9/10 14:25
 * @version 1.0.0
 *
 *          隐式转换
 */
object Implicits {
    def main(args: Array[String]): Unit = {
        implicit def double2int(d: Double): Int = d.toInt

        implicit def double2string(d: Double): String = d.toString

        val a: Int = 10.2
        val b: String = 10.2
        println(a + b)

        /** 隐式转换函数 */
        implicit def getRichData(a: Int): RichData = new RichData(a)

        val ago = "ago"
        val later = "later"

        3.days(ago)
        3 days ago

        3 years ago
        3 years later

        val string = new File("./test.txt").read
        println(string)

        implicit val num: Int = 10 // 隐式值
        foo1
        foo2(10)
        foo2(10)(20)
        foo3(10)
        foo3(10)()
        foo3(10)(20)

        foo // 隐式值优先查找当前作用域，若没有则查找相关类型的伴生对象
    }

    /** 含有隐式值参数的函数 */
    def foo1(implicit a: Int): Unit = println(a)

    /** 隐式值参数、柯里化 */
    def foo2(a: Int)(implicit b: Int): Unit = println(a + b)

    /** 隐式值参数、柯里化、默认值 */
    def foo3(a: Int)(implicit b: Int = 20): Unit = println(a + b)

    /**
     * 隐式类
     *
     * @param a 构造器只接收一个参数
     *
     *          不能写在最外层
     */
    implicit class ImplicitClass(a: Int) {
        def years(s: String): Unit = println(s"s$a years" + s)
    }

    /**
     * 通过隐式类直接读取文件
     *
     * @param file 读取的文件对象
     */
    //noinspection SourceNotClosed
    implicit class RichFile(file: File) {
        def read: String = Source.fromFile(file).mkString
    }

    def foo(implicit a: A[B]): Unit = println("implicits")

}

class RichData(i: Int) {
    def days(s: String): Unit = println(s"$i days" + s)
}

class B

class A[B]

object B {
    implicit val a: A[B] = new A[B]
}
