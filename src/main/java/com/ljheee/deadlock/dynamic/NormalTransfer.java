package com.ljheee.deadlock.dynamic;

/**
 * 动态的锁顺序死锁
 * 比如使用参数引用的对象加锁，由于无法控制参数的顺序，因此可能出现顺序死锁的问题。比如A向B赚钱是加锁顺序A->B，但是两者相互赚钱时顺序相反，可能死锁：
 */
public class NormalTransfer implements ITransfer {


    @Override
    public void transferMoney(String fromAccount, String toAccount, String amount) throws Exception {
        synchronized (fromAccount) {
            Thread.sleep(100);
            synchronized (toAccount) {
                System.out.println(fromAccount + "转出给" + toAccount);
            }
        }
        System.out.println("完成转账");
    }


    public static void main(String[] args) throws Exception {
        String fromAccount = "张三";
        String toAccount = "李四";
        String amount = "100w";

        ITransfer transfer = new NormalTransfer();

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
        ls2zs.start(); // 出现死锁

    }
}
