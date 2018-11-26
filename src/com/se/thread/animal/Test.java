package com.se.thread.animal;

/**
 * 龟兔赛跑：2000米 
 * 要求：
 *     (1)兔子每 0.1 秒 5 米的速度，每跑20米休息1秒;
 *     (2)乌龟每 0.1 秒跑 2 米，不休息；
 *     (3)其中一个跑到终点后另一个不跑了！
 * 程序设计思路：
 *     (1)创建一个Animal动物类，继承Thread，编写一个running抽象方法，重写run方法，把running方法在run方法里面调用。
 *     (2)创建Rabbit兔子类和Tortoise乌龟类，继承动物类
 *     (3)两个子类重写running方法
 *     (4)本题的第3个要求涉及到线程回调。需要在动物类创建一个回调接口，创建一个回调对象。
 *
 * @author: wtz
 * Date:2018/11/26
 */
public class Test {

    public static void main(String[] args) {
        // 实例化乌龟和兔子
        Tortoise tortoise = new Tortoise();
        Rabbit rabbit = new Rabbit();

        // 回调方法的使用，谁先调用calltoback方法，另一个就不跑了
        LetOneStop letOneStop1 = new LetOneStop(tortoise);
        // 让兔子的回调方法里面存在乌龟对象的值，可以把乌龟stop
        rabbit.calltoback = letOneStop1;

        LetOneStop letOneStop2 = new LetOneStop(rabbit);
        // 让乌龟的回调方法里面存在兔子对象的值，可以把兔子stop
        tortoise.calltoback = letOneStop2;
        // 开始跑
        tortoise.start();
        rabbit.start();

    }
}
