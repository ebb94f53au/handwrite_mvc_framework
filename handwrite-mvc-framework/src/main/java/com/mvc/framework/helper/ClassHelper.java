package com.mvc.framework.helper;

import com.mvc.framework.annotation.Controller;
import com.mvc.framework.annotation.Service;
import com.mvc.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 借助 ClassUtil 来实现 ClassHelper 助手类
 * ClassHelper 助手类在自身被加载的时候通过 ConfigHelper 助手类获取应用的基础包名,
 * 然后通过 ClassUtil 工具类来获取基础包名下所有类, 存储到 CLASS_SET 集合中
 * @author study
 * @create 2019-11-07 20:29
 */
public final class ClassHelper {

    /**
     * 定义类集合（存放基础包名下的所有类）
     */
    private static final Set<Class<?>> CLASS_SET ;

    static{
        //获取基础包名
        String appBasePackage = ConfigHelper.getAppBasePackage();
        //获取
        CLASS_SET= ClassUtil.getClassSet(appBasePackage);
    }
    //获得定义类集合
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }
    /**
     * 获取基础包名下所有 Service 类
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> serviceSet=new HashSet<>();
        CLASS_SET.forEach(clazz->{
            if(clazz.isAnnotationPresent(Service.class)){
                serviceSet.add(clazz);
            }
        });
        return serviceSet;
    }
    /**
     * 获取基础包名下所有 Controller 类
     */
    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> controllerSet=new HashSet<>();
        CLASS_SET.forEach(clazz->{
            if(clazz.isAnnotationPresent(Controller.class)){
                controllerSet.add(clazz);
            }
        });
        return controllerSet;
    }

    /**
     * 获取基础包名下所有 Bean 类（包括：Controller、Service）
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> beanSet=new HashSet<>();
        beanSet.addAll(getServiceClassSet());
        beanSet.addAll(getControllerClassSet());
        return beanSet;
    }

    /**
     * 获取基础包名下某父类的所有子类 或某接口的所有实现类
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> set=new HashSet<>();
        CLASS_SET.forEach(clazz->{
            //isAssignableFrom() 指 superClass 和 cls 是否相同或 superClass 是否是 cls 的父类/接口
            if(superClass.isAssignableFrom(clazz)&&!superClass.equals(clazz)){
                set.add(clazz);
            }
        });
        return set;
    }

    /**
     * 获取基础包名下带有某注解的所有类
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> set=new HashSet<>();
        CLASS_SET.forEach(clazz->{
            if(clazz.isAnnotationPresent(annotationClass)){
                set.add(clazz);
            }
        });
        return set;
    }
}
