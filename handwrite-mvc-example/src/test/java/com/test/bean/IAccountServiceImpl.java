package com.test.bean;

/**
 * @author study
 * @create 2019-11-11 13:01
 */
public class IAccountServiceImpl implements IAccountService{
    @Override
    public void transfer() {
        System.out.println("IAccountService  执行");
    }
}
