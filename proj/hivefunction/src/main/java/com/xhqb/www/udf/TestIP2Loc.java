package com.xhqb.www.udf;

public class TestIP2Loc {
    public static void main(String[] args) throws Exception {
        IP2Loc ipTest = new IP2Loc();
        System.out.println(ipTest.evaluate("1.8.17.2"));
    }
}
