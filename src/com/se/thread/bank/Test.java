package com.se.thread.bank;

/**
 * 两个人AB通过一个账户A在柜台取钱和B在ATM机取钱！
 * 程序分析：
 * 钱的数量要设置成一个静态的变量，两个人要取的同一个对象值。
 *
 * @author: wtz
 * Date:2018/11/26
 */
public class Test {

    public static void main(String[] args) {
        Bank bank = new Bank();
        Person p1 = new Person(bank, "", 100);
        Person p2 = new Person(bank, "ATM", 190);

        p1.start();
        p2.start();
    }

}
