package datarw

import java.sql.DriverManager

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author tiankx
 * @date 2020/11/23 20:34
 * @version 1.0.0
 */
object MysqlRW {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("MysqlRW").setMaster("local[*]")
    val sc = new SparkContext(conf)
    /** 数据库连接参数 */
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://server01:3306/rdd"
    val userName = "root"
    val passWd = "root"
    /** 读取数据 */
    val jdbcRDD = new JdbcRDD(
      sc,
      () => {
        Class.forName(driver)
        DriverManager.getConnection(url, userName, passWd)
      },
      "select name, id from user where id >= ? and id <= ?",
      1,
      20,
      2,
      result => (result.getInt(1), result.getString(2))
    )
    jdbcRDD.collect.foreach(println)

    val numRDD = sc.parallelize(Array(2, 3, 4))
    /** 写入数据 */
    numRDD.foreachPartition(it => {
      Class.forName(driver)
      val conn = DriverManager.getConnection(url, userName, passWd)
      it.foreach(x => {
        val statement = conn.prepareStatement("insert into user values(?,?)")
        statement.setInt(1,x)
        statement.setInt(2, x)
        statement.executeUpdate()
      })
    })
    sc.stop
  }
}
