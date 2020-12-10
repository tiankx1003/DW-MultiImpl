package com.tian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/12/10 19:03
 */
public class Test2 {
    private static Connection conn=Test.getConnnection();
    private static PreparedStatement ps;
    private static ResultSet rs;
    public static void getAll(String tablename)
    {
        String sql="select * from "+tablename;
        System.out.println(sql);
        try {
            ps=Test.prepare(conn, sql);
            rs=ps.executeQuery();
            int columns=rs.getMetaData().getColumnCount();
            while(rs.next())
            {
                for(int i=1;i<=columns;i++)
                {
                    System.out.print(rs.getString(i));
                    System.out.print("\t\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String tablename="gmall.ads_back_count";
        Test2.getAll(tablename);
    }

}

