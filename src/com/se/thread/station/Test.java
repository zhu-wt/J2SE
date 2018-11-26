package com.se.thread.station;

/**
 * @author: wtz
 * Date:2018/11/26
 */
public class Test {

    /**
     * java多线程同步锁的使用
     * 示例：三个售票窗口同时出售10张票
     * @param args
     */
    public static void main(String[] args) {

        Station one = new Station("one ");
        Station two = new Station("two ");
        Station three = new Station("three ");

        one.start();
        two.start();
        three.start();
    }
}
