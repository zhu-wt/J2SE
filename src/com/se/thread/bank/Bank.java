package com.se.thread.bank;

import java.util.Objects;

/**
 * 两个人AB通过一个账户A在柜台取钱和B在ATM机取钱！
 * 程序分析：
 * 钱的数量要设置成一个静态的变量，两个人要取的同一个对象值。
 *
 * @author: wtz
 * Date:2018/11/26
 */
public class Bank {

    /**
     * 假设一个账户有1000块钱
     */
    public static double money = 2000;

    /**
     * 柜台Counter取钱的方法
     */
    private void Counter(double money,String threadName) {
        Bank.money -= money;
        System.out.println(threadName + "柜台取钱" + money + "元，还剩" + Bank.money + "元！");
    }

    /**
     * ATM取钱的方法
     */
    private void ATM(double money,String threadName) {
        Bank.money -= money;
        System.out.println(threadName + "ATM取钱" + money + "元，还剩" + Bank.money + "元！");
    }

    /**
     * 提供一个对外取款途径，防止直接调取方法同时取款时，并发余额显示错误
     */
    public synchronized void outMoney(double money, String mode,String threadName) throws Exception {
        if (money > Bank.money) {
            //校验余额是否充足
            throw new Exception(threadName + "取款金额" + money + ",余额只剩" + Bank.money + "，取款失败");
        }
        if (Objects.equals(mode, "ATM")) {
            ATM(money,threadName);
        } else {
            Counter(money,threadName);
        }

    }
}
