package com.mvc.framework.bean;

import java.lang.reflect.Method;

/**
 * Handler类为一个处理器, 封装了Controller的Class对象和Method方法.
 * @author study
 * @create 2019-11-09 13:59
 */
public class Handler {
    /**
     * Controller类
     */
    private Class<?> controllerClass;
    /**
     * Colltroller方法
     */
    private Method controllerMethod;

    public Handler(Class<?> controllerClass, Method controllerMethod) {
        this.controllerClass = controllerClass;
        this.controllerMethod = controllerMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }
}
