package com.hspedu.hspliving;

/**
 * @author yangda
 * @create 2024-03-08-19:24
 * @description:
 */
public class TimeTest {
    public static void main(String[] args) {
        long time1 = System.currentTimeMillis();
        System.out.println("System.currentTimeMillis() = " + time1);
        long time2 = time1 / 1000;
        System.out.println("time = " + time2);
    }
}
