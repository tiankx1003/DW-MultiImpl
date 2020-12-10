package com.tian;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/12/10 19:00
 */
public class Test {
    private static String driverName ="org.apache.hive.jdbc.HiveDriver";   // 此Class 位于 hive-jdbc的jar包下
    private static String Url="jdbc:hive2://222.22.91.81:10000/";    //填写hive的IP，之前在配置文件中配置的IP
    private static Connection conn;
    public static Connection getConnnection()
    {
        try
        {
            Class.forName(driverName);
            conn = DriverManager.getConnection(Url,"","");        //只是连接hive, 用户名可不传
        }
        catch(ClassNotFoundException e)  {
            e.printStackTrace();
            System.exit(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static PreparedStatement prepare(Connection conn, String sql) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

    public static void main(String[] args) {

    }
}
