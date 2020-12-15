package com.weshare.spark.utils

/**
 * created by chao.guo on 2020/8/28
 **/
import java.sql.{Connection, ResultSet, Statement}
import java.util.Properties

import com.alibaba.druid.pool.DruidDataSource
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession
import com.alibaba.druid.pool.DruidDataSourceFactory
object DruidDataSourceUtils {
  var CM_DruidDataSource: DruidDataSource = _
  var PRO_MYSQL_DATA_SOURCE:DruidDataSource = _
  var IMPALA_DATA_SOURCE:DruidDataSource = _
  var HIVE_DATA_SOURCE:DruidDataSource = _
   def initDataSource(spark:SparkSession, configPath:String,engine_type:String):Unit={
    val pro = new  Properties()
    val inputSteam = FileSystem.get(spark.sparkContext.hadoopConfiguration).open(new Path(configPath))
    pro.load(inputSteam)
    inputSteam.close()
     engine_type.toLowerCase match {
       case "cm_mysql" =>CM_DruidDataSource =DruidDataSourceFactory.createDataSource(pro).asInstanceOf[DruidDataSource]
       case "pro_mysql"=>PRO_MYSQL_DATA_SOURCE =DruidDataSourceFactory.createDataSource(pro).asInstanceOf[DruidDataSource]
       case "impala" | "kudu"=> IMPALA_DATA_SOURCE =DruidDataSourceFactory.createDataSource(pro).asInstanceOf[DruidDataSource]
       case "hive" =>HIVE_DATA_SOURCE =DruidDataSourceFactory.createDataSource(pro).asInstanceOf[DruidDataSource]
       case _=>null
     }

  }



  // 获取连接
  def getConnection(engine_type:String): Connection = {
    engine_type.toLowerCase match {
      case "cm_mysql" =>CM_DruidDataSource.getConnection
      case "pro_mysql" | "mysql" =>PRO_MYSQL_DATA_SOURCE.getConnection
      case "impala" | "kudu"=> IMPALA_DATA_SOURCE.getConnection
      case "hive" =>HIVE_DATA_SOURCE.getConnection
      case _=>null
    }

  }

def dataSourceClose()={
  if(CM_DruidDataSource.isKeepAlive){
    CM_DruidDataSource.close()
  }
  if(PRO_MYSQL_DATA_SOURCE.isKeepAlive){
    PRO_MYSQL_DATA_SOURCE.close()
  }
  if(IMPALA_DATA_SOURCE.isKeepAlive){
    IMPALA_DATA_SOURCE.close()
  }
  if(HIVE_DATA_SOURCE.isKeepAlive){
    HIVE_DATA_SOURCE.close()
  }

}

  //关闭连接
  def close(rs: ResultSet, statement: Statement, conn: Connection): Unit = {
    try {
      if (null != rs) rs.close
      if (null != statement) statement.close
      if (null != conn) conn.close
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
    }
  }

}
