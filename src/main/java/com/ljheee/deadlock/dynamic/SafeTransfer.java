package com.ljheee.deadlock.dynamic;

/**
 * 顺序加锁+时赛锁
 */
public class SafeTransfer implements ITransfer {

    private Object tieLock = new Object();//时赛锁

    @Override
    public void transferMoney(String fromAccount, String toAccount, String amount) throws Exception {
        int from = System.identityHashCode(fromAccount);
        int to = System.identityHashCode(toAccount);
        if (from > to) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    System.out.println("transfer " + fromAccount + "to " + toAccount);
                }
            }
        } else if (from < to) {
            synchronized (toAccount) {
                synchronized (fromAccount) {
                    System.out.println("transfer " + fromAccount + "to " + toAccount);
                }
            }
        }
        /**
         * 两个参数hashCode相同，则先获取加时赛锁，然后在获取两个账号的锁
         * 注意 加时赛锁 一定是全局唯一，否则没意义，相互等锁的线程仍然可能形成环状
         */
        else {
            synchronized (tieLock) {
                synchronized (fromAccount) {
                    synchronized (toAccount) {
                        System.out.println("transfer " + fromAccount + "to " + toAccount);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String fromAccount = "张三";
        String toAccount = "李四";
        String amount = "100w";

        ITransfer transfer = new SafeTransfer();

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
