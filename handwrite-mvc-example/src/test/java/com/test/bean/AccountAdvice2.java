package com.test.bean;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author study
 * @create 2019-11-11 22:34
 */
public class AccountAdvice2  implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("执行前");
        return methodProxy.invokeSuper(o,objects);
    }
}
