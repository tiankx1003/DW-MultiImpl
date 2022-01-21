package com.xhqb.www.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IP2Loc extends UDF {
    public static List<String> map = new ArrayList();
    public static long[] start_from_index;
    public static long[] end_to_index;
    public static Map<Long, String> idcCache = new HashMap();
    public static Map<Long, String> regionCache = new HashMap();
    public static Map<Long, String> cityCache = new HashMap();

    private void LoadIPLocation() throws Exception {

        FileInputStream file = new FileInputStream("ip.csv");
        InputStreamReader inputStreamReader = new InputStreamReader(file,"utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String s = null;
        while (true) {
            s = bufferedReader.readLine();
            if (s == null) {
                break;
            }
            map.add(s);
        }

        bufferedReader.close();
    }

    public static int binarySearch(long[] start, long[] end, long ip) {
        int low = 0;
        int high = start.length - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            //System.out.println(middle);
            if ((ip >= start[middle]) && (ip <= end[middle]))
                return middle;
            if (ip < start[middle])
                high = middle - 1;
            else {
                low = middle + 1;
            }
        }
        return -1;
    }

    public static long ip2long(String ip) {
        if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String[] ips = ip.split("[.]");
            long ipNum = 0L;
            if (ips == null) {
                return 0L;
            }
            for (int i = 0; i < ips.length; i++) {
                ipNum = ipNum << 8 | Long.parseLong(ips[i]);
            }

            return ipNum;
        }
        return 0L;
    }

    public static String chinese2CharString(String message) {
        //1、将中字符串转成字节数组，
        //2、然后将字节数组转为对应的char拼接成字符串
        //3、再将拼接后的字符串写入文件
        byte[] bytes3 = message.getBytes(StandardCharsets.UTF_8);
        char[] chars1 = new char[bytes3.length];
        for (int i = 0; i < bytes3.length; i++) {
            chars1[i] = (char) bytes3[i];
        }
        return new String(chars1);
    }

    public String evaluate(String ip) throws Exception {
        long ipLong = ip2long(ip);
        //System.out.println(ipLong);

        if (map.size() == 0) {
            LoadIPLocation();
            start_from_index = new long[map.size()];
            end_to_index = new long[map.size()];
            for (int i = 0; i < map.size(); i++) {
                //StringTokenizer token = new StringTokenizer((String) map.get(i), ",");
                String[] ipStr = map.get(i).split(",");
                start_from_index[i] = Long.parseLong(ipStr[1]);
                end_to_index[i] = Long.parseLong(ipStr[2]);
            }

        }

        int ipindex = 0;
        ipindex = binarySearch(start_from_index, end_to_index, ipLong);

        //System.out.println(ipindex);

        if (ipindex == -1) {
            return null;
        }

        String[] location = ((String) map.get(ipindex)).split(",");
        System.out.println(location[3] + "," + location[4] + "," + location[5]);
        return location[3] + "," + location[4] + "," + location[5];
    }


}