package com.tian

/**
 * @author tiankx
 * @date 2020/10/10 17:11
 * @version 1.0.0
 */
object ExceptionDemo { // TODO: 2020-10-10 17:11:23 异常
  def main(args: Array[String]): Unit = {
    Thread.sleep(1000)

    /** try-catch捕捉处理异常 */
    try {
      val a = 1 / 0
    } catch {
      case e: ArithmeticException => println(e)
      case e: RuntimeException => println(e)
      case e: Exception => println(e)
      case _ =>
    } finally {
      println("finally")
    }

    /** 抛出异常 */
    throw new IllegalArgumentException("参数非法")

    @throws(classOf[IllegalArgumentException])
    def f0(a: String) = a.toInt
  }
}
