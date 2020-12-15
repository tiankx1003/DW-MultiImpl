package com.tian;

import java.sql.*;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/12/13 9:43
 */
public class TestHiveJDBC {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // String driver = "org.apache.hive.jdbc.HiveDriver";
        // String url = "jdbc:hive2://192.168.2.201:10000/";
        // Class.forName(driver);
        // Connection conn = DriverManager.getConnection(url, "tiankx", "tiankx");
        // PreparedStatement prepareStatement = conn.prepareStatement("show tables");
        // ResultSet resultSet = prepareStatement.executeQuery();
        // while (resultSet.next()) {
        //     for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
        //         System.out.println(resultSet.getString(i));
        //     }
        // }
        // prepareStatement.close();
        // conn.close();

        new ConnectHive().showtDb();
    }
}
