package com.mvc.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 这是一个代理工厂类, 我们通过这个类来梳理ProxyChain、AspectProxy的代理逻辑.
 * 当调用 ProxyFactory.createProxy(final Class<?> targetClass, final List proxyList) 方法来创建一个代理对象后,
 * 每次执行代理方法时都会调用 intercept() 方法,
 * 从而创建一个 ProxyChain 对象,
 * 并调用该对象的 doProxyChain() 方法.
 * 调用doProxyChain()方法时会首先递归的执行增强, 最后再执行目标方法.
 * @author study
 * @create 2019-11-11 23:13
 */
public class ProxyFactory {

    /**
     * 输入一个目标类和一组Proxy接口实现,
     * 输出一个代理对象
     * <T> T 方法返回值的类型由调用者决定
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {

        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            /**
             * 匿名内部类实现
             * 代理方法, 每次调用目标方法时都会先创建一个 ProxyChain 对象, 然后调用该对象的 doProxyChain() 方法.
             */
            @Override
            public Object intercept(Object object, Method method, Object[] param, MethodProxy methodProxy) throws Throwable {

                return new ProxyChain(targetClass,object,method,methodProxy,param,proxyList).doProxyChain();
            }
        });
    }

}
