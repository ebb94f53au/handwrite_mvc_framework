package com.mvc.framework.helper;

import com.mvc.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * BeanHelper 在类加载时就会创建一个Bean容器 BEAN_MAP,
 * 然后获取到应用中所有bean的Class对象,
 * 再通过反射创建bean实例, 储存到 BEAN_MAP 中.
 * @author study
 * @create 2019-11-07 21:09
 */
public final class BeanHelper {
    /**
     * BEAN_MAP相当于一个Spring容器, 拥有应用所有bean的实例
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static{
        //获取应用中所有的bean的class对象
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        //遍历将类实例化对象放入map中
        beanClassSet.forEach(beanClass->{
            Object o = ReflectionUtil.newInstance(beanClass);
            setBean(beanClass,o);
        });
    }

    /**
     * 获取 Bean 容器
     */
    public static Map<Class<?>, Object> getBeanMap() {

        return BEAN_MAP;
    }

    /**
     * 获取 Bean 实例
     * @SuppressWarnings("unchecked")
     * 告诉编译器忽略 unchecked 警告信息，如使用List，ArrayList等未进行参数化产生的警告信息。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if(!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }
    /**
     * 添加 Bean 实例
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }


}
