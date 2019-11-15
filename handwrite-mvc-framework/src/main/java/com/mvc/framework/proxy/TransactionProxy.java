package com.mvc.framework.proxy;

import com.mvc.framework.annotation.Transactional;
import com.mvc.framework.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * TransactionProxy 为事务切面类, 同样实现了Proxy接口,
 * 其 doProxy() 方法就是先判断代理方法上有没有 @Transactional 注解,
 * 如果有就加上事务管理, 没有就直接执行.
 * @author study
 * @create 2019-11-15 21:26
 */
public class TransactionProxy implements Proxy{
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    /**
     * 先判断代理方法上有没有 @Transactional 注解,
     * 如果有就加上事务管理, 没有就直接执行.
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
            Object result;
            Class<?> targetClass = proxyChain.getTargetClass();
            Method targetMethod = proxyChain.getTargetMethod();
            Object[] methodParams = proxyChain.getMethodParams();

            if(targetMethod.isAnnotationPresent(Transactional.class)){
                try {
                    //开始事务
                    DatabaseHelper.beginTransaction();
                    LOGGER.debug("begin Transaction");
                    result = proxyChain.doProxyChain();
                    //提交事务
                    DatabaseHelper.commitTransaction();
                    LOGGER.debug("commit Transaction");
                } catch (Exception e) {
                    //异常就回滚
                    DatabaseHelper.rollbackTransaction();
                    LOGGER.debug("rollback Transaction",e);
                    throw e;
                }
            }else{
                result = proxyChain.doProxyChain();
            }
            return result;
        }
}
