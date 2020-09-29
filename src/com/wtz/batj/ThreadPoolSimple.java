package com.wtz.batj;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * java 并发库的线程池
 *
 * @author: wtz
 * Date:2020/5/25
 */
public class ThreadPoolSimple {

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 8,
                200, TimeUnit.MILLISECONDS
                , new ArrayBlockingQueue<>(5));

        for (int i = 0; i < 15; i++) {
            MyTask myTask = new MyTask(i);
            executor.execute(myTask);
            System.out.println("线程池中的线程数目:" + executor.getPoolSize()
                    + "队列中等待执行的任务数目：" + executor.getQueue().size()
                    + "已完成的数目：" + executor.getCompletedTaskCount());
        }
    }

}

class MyTask implements Runnable {

    private int taskNum;

    public MyTask(int taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public void run() {
        System.out.println("正在执行task" + taskNum);

        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task" + taskNum + "执行完毕");
    }
}
