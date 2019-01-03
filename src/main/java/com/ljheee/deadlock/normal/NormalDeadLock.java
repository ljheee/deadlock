package com.ljheee.deadlock.normal;

/**
 * 普通死锁：加锁顺序不当造成
 */
public class NormalDeadLock {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    static Runnable runnable1 = () -> {
        try {
            synchronized (lock1) {
                Thread.sleep(100);
                synchronized (lock2) {
                    //doSomethoing();
                    System.out.println(Thread.currentThread().getName());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    static Runnable runnable2 = () -> {
        try {
            synchronized (lock2) {
                Thread.sleep(100);
                synchronized (lock1) {
                    //doSomethoing();
                    System.out.println(Thread.currentThread().getName());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };


    public static void main(String[] args) {

        new Thread(runnable1).start();
        new Thread(runnable2).start();
        // jps
        // jstack pid

    }



}
