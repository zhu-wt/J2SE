package com.se.thread.bank;

/**
 * @author: wtz
 * Date:2018/11/26
 */
public class Person extends Thread {

    private Bank bank;
    private String mode;
    private double money;

    public Person(Bank bank, String mode, double money) {
        this.bank = bank;
        this.mode = mode;
        this.money = money;
    }

    @Override
    public void run() {
        while (Bank.money > money) {
            try {
                bank.outMoney(money,mode,this.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
