package com.mvc.framework.helper;

import com.mvc.framework.annotation.Autowired;
import com.mvc.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 *  我们需要做的就是遍历Bean容器中的所有bean,
 *  为所有带 @Autowired 注解的属性注入实例. 这个实例从Bean容器中获取.
 * @author study
 * @create 2019-11-07 21:20
 */
public final  class IocHelper {
    /**
     * 遍历bean容器所有bean的属性, 为所有带@Autowired注解的属性注入实例
     */
    static{
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        //遍历类class对象
        for (Map.Entry<Class<?>, Object> co : beanMap.entrySet()) {
            Field[] declaredFields = co.getKey().getDeclaredFields();
            //遍历类class对象的属性
            for (Field field : declaredFields) {
                //判断属性上是否有autowired注解
                if(field.isAnnotationPresent(Autowired.class)){
                    //根据属性的类型来加载属性
                    Class<?> type = field.getType();
                    //如果是接口得到他的实现类
                    Class<?> implementClass = findImplementClass(type);

                    ReflectionUtil.setField(co.getValue(),field,beanMap.get(implementClass));



                }
            }
        }

    }
    /**
     * 获取接口对应的实现类
     */
    public static Class<?> findImplementClass(Class<?> interfaceClass) {
        //先将传入类赋值给implementClass
        Class<?> implementClass = interfaceClass;
        //查找传入类的子类或实现类的集合
        Set<Class<?>> classSetBySuper = ClassHelper.getClassSetBySuper(interfaceClass);
        //如果不为空
        if(!classSetBySuper.isEmpty()){
            //将集合中第一个赋值给implementClass
            implementClass=classSetBySuper.iterator().next();
        }
        return implementClass;
    }
}
