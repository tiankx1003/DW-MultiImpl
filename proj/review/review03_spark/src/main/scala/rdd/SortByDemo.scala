package rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/10/28 0:00
 * @version 1.0.0
 */
object SortByDemo {
  implicit val ord: Ordering[User] = new Ordering[User] {
    override def compare(x: User, y: User): Int = x.age - y.age
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 2)
    //    val result = rdd1.sortBy(x => x, true)
    val rdd2: RDD[User] = sc.parallelize(Array(User(20, "a"), User(30, "b"), User(40, "c")))
    //    val rdd3: RDD[User] = rdd2.sortBy(user => (user.age, user.name))
    val rdd4 = rdd2.sortBy(user => user)
    val rdd5 = rdd1.sample(false, 0.9)

    /** 自定义类需要提供隐式值Ordering */
    rdd4.collect.foreach(println)
    sc.stop()
  }
}


case class User(age: Int, name: String)