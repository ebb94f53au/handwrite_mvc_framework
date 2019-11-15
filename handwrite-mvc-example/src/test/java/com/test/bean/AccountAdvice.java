package com.test.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author study
 * @create 2019-11-11 13:02
 */
public class AccountAdvice implements InvocationHandler
{
    IAccountService target;

    public AccountAdvice(IAccountService target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        return method.invoke(target,args);
    }

    public void before (){
        System.out.println("before！！！！！！！");

    }
}
