package com.mvc.framework.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * 通过反射实例化对象，使用方法，设置属性值
 * @author study
 * @create 2019-11-07 20:52
 */
public final  class ReflectionUtil {
    private static final Logger LOGGER=LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> cls) {

        Object o = null;
        try {
            o = cls.newInstance();
        } catch (Exception e) {
            LOGGER.error(" create newInstance failure",e);
            throw new RuntimeException(e);
        }
        return o;

    }

    /**
     * 创建实例（根据类名）
     */
    public static Object newInstance(String className) {
        Class<?> aClass = ClassUtil.loadClass(className);

        return newInstance(aClass);
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object invoke =null ;
        try {
            //允许调用私有的方法
            method.setAccessible(true);
            invoke = method.invoke(obj, args);
        } catch (Exception e) {
            LOGGER.error(" invoke method failure",e);
            throw new RuntimeException(e);
        }
        return invoke;
    }

    /**
     * 设置成员变量的值
     */
    public static void setField(Object obj, Field field, Object value) {
        //允许调用私有的属性
        field.setAccessible(true);
        try {
            field.set(obj,value);
        } catch (IllegalAccessException e) {
            LOGGER.error("set field failure",e);
            throw new RuntimeException(e);
        }

    }
}
