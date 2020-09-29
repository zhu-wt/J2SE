package com.wtz.batj;

/**
 * 死锁应用
 *
 * @author: wtz
 * Date:2020/5/25
 */
public class DealLockSimple extends Thread {

    private String lockA;
    private String lockB;


    public DealLockSimple(String name, String lockA, String lockB) {
        super(name);
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {

        synchronized (lockA) {
            System.out.println(this.getName() + "--->" + lockA);

            try {
                Thread.sleep(1000L);
                synchronized (lockB) {
                    System.out.println(this.getName() + "-------->" + lockB);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        String lockA = "lockA";
        String lockB = "lockB";

        DealLockSimple th1 = new DealLockSimple("th1", lockA, lockB);
        DealLockSimple th2 = new DealLockSimple("th2", lockB, lockA);
        th1.start();
        th2.start();

//        th1.join();
//        th2.join();
        th1.run();
        th2.run();
    }
}
