package com.siyang.aspect;

import com.mvc.framework.annotation.Aspect;
import com.mvc.framework.proxy.AspectProxy;

import java.lang.reflect.Method;

/**
 * @author study
 * @create 2019-11-12 21:03
 */
@Aspect(pkg = "com.siyang.controller",cls = "UserController")
public class UserAspect extends AspectProxy{

    /**
     * 切入点判断
     */
    @Override
    public boolean intercept(Method method, Object[] params) throws Throwable {
        return method.getName().equals("getAllView");
    }

    @Override
    public void before(Method method, Object[] params) throws Throwable {
        System.out.println("UserAspect before");
    }

    @Override
    public void after(Method method, Object[] params) throws Throwable {
        System.out.println("UserAspect after");

    }

}
