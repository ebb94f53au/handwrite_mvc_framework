package com.mvc.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * AspectProxy是一个切面抽象类, 实现了Proxy接口, 类中定义了切入点判断和各种增强.
 * 当执行 doProxy() 方法时, 会先进行切入点判断, 再执行前置增强,
 * 代理链的下一个doProxyChain()方法, 后置增强等.
 * @author study
 * @create 2019-11-11 23:01
 */
public class AspectProxy implements Proxy {
    private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        //得到目标的class对象
        Class<?> targetClass = proxyChain.getTargetClass();
        //得到目标的方法
        Method targetMethod = proxyChain.getTargetMethod();
        //得到目标的方法参数
        Object[] methodParams = proxyChain.getMethodParams();

        //开始增强
        begin();
        try {
            //切入点判断
            if(intercept(targetMethod,methodParams)){
                //强制增强
                before(targetMethod, methodParams);
                //此处与ProxyChain的doProxyChain方法中的doProxy产生了递归
                result = proxyChain.doProxyChain();
                //后置增强
                after(targetMethod,methodParams);
            }else{
                //没有切入点直接执行
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            logger.error("proxy failure", e);
            //异常循环
            error(targetMethod, methodParams, e);
            throw e;
        } finally {
            //最终循环
            end();
        }
        return result;
    }

    /**
     * 开始增强
     */
    public void begin() {
    }

    /**
     * 切入点判断
     */
    public boolean intercept(Method method, Object[] params) throws Throwable {
        return true;
    }

    /**
     * 前置增强
     */
    public void before(Method method, Object[] params) throws Throwable {
    }

    /**
     * 后置增强
     */
    public void after(Method method, Object[] params) throws Throwable {
    }

    /**
     * 异常增强
     */
    public void error(Method method, Object[] params, Throwable e) {
    }

    /**
     * 最终增强
     */
    public void end() {
    }
}
