package com.ljheee.deadlock.dynamic;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  加入随机，减少活锁
 */
public class SafeTransferTry implements ITransfer {


    @Override
    public void transferMoney(String fromAccount, String toAccount, String amount) throws Exception {

        UserAccount from = new UserAccount(fromAccount);
        UserAccount to = new UserAccount(toAccount);
        Random random = new Random();

        String threadName = Thread.currentThread().getName();

        while (true) {
            try {
                if (from.getLock().tryLock()) {//先锁 转出
                    System.out.println(threadName + "  get " + from.getName());
                    try {
                        if (to.getLock().tryLock()) {//再锁 转入
                            System.out.println(threadName + "  get " + to.getName());

                            // 实际业务
                            System.out.println(fromAccount + "转出给" + toAccount + ",金额：" + amount);
                            break;
                        }
                    } finally {
                        to.getLock().unlock();
                    }
                }
            } finally {
                from.getLock().unlock();
            }
        }
        //随机睡眠
        Thread.sleep(random.nextInt(2));
    }

    public static void main(String[] args) throws Exception {
        String fromAccount = "张三";
        String toAccount = "李四";
        String amount = "100w";

        ITransfer transfer = new SafeTransferTry();

        //张三 转给李四
        Thread zs2ls = new Thread(() -> {
            try {
                transfer.transferMoney(fromAccount, toAccount, amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //李四 转给张三
        Thread ls2zs = new Thread(() -> {
            try {
                transfer.transferMoney(toAccount, fromAccount, amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        zs2ls.start();
        ls2zs.start();
    }


}

class UserAccount {
    String name;
    Lock lock = new ReentrantLock();//底层依赖AQS管理队列排队、阻塞、唤醒等，由内部类继承AQS。具体资源获取，由ReentrantLock实现。

    public UserAccount(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }
}