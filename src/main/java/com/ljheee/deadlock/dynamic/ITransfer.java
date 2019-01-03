package com.ljheee.deadlock.dynamic;

/**
 *  装帐 接口
 */
public interface ITransfer {
    void transferMoney(String fromAccount, String toAccount,String amount) throws Exception;
}
