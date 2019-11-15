package com.mvc.framework.helper;

import com.mvc.framework.annotation.Aspect;
import com.mvc.framework.annotation.Service;
import com.mvc.framework.proxy.AspectProxy;
import com.mvc.framework.proxy.Proxy;
import com.mvc.framework.proxy.ProxyFactory;
import com.mvc.framework.proxy.TransactionProxy;
import com.mvc.framework.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.plugin2.message.helper.ProxyHelper;

import java.lang.annotation.Target;
import java.util.*;

/**
 * AopHelper 助手类用来初始化整个AOP框架
 * 逻辑如下:
 *
 * 框架中所有Bean的实例都是从Bean容器中获取, 然后再执行该实例的方法,
 * 基于此, 初始化AOP框架实际上就是用代理对象覆盖掉Bean容器中的目标对象,
 * 这样根据目标类的Class对象从Bean容器中获取到的就是代理对象, 从而达到了对目标对象增强的目的.
 * @author study
 * @create 2019-11-12 19:53
 */
public class AopHelper {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(AopHelper.class);

    static {


        try {
            //得到切面类-目标类的集合映射
            Map<Class<?>, Set<Class<?>>> aspectMap = createAspectMap();
            for (Map.Entry<Class<?>, Set<Class<?>>> classSetEntry : aspectMap.entrySet()) {
                System.out.println(classSetEntry.getKey());
                System.out.println(classSetEntry.getValue());
            }
            //得到 目标类-切面类对象列表的集合映射
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(aspectMap);
            //把切面对象织入到目标类中, 创建代理对象
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                //目标类
                Class<?> key = targetEntry.getKey();
                //切面类列表
                List<Proxy> value = targetEntry.getValue();
                //
                Object proxy = ProxyFactory.createProxy(key, value);
                //覆盖Bean容器中的目标类对应实例，下次获取就是代理对象了
                BeanHelper.setBean(key,proxy);
            }


        } catch (Exception e) {
            LOGGER.error("aop failure", e);
        }

    }


    /**
     * 获取切面类-目标类集合的映射
     */
    private static Map<Class<?>, Set<Class<?>>> createAspectMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> aspectMap = new HashMap<>();
        //从基础包的所有类集合下，寻找AspectProxy的子类集合->切面类
        Set<Class<?>> classSetBySuper = ClassHelper.getClassSetBySuper(AspectProxy.class);
        //遍历切面类集合
        classSetBySuper.forEach(classBySuper -> {
            if (classBySuper.isAnnotationPresent(Aspect.class)) {
                Aspect annotation = classBySuper.getAnnotation(Aspect.class);
                try {
                    //通过注解传递的包名和类名 找到与该切面对应的目标类的集合
                    Set<Class<?>> targetClassSet = createTargetClassSet(annotation);
                    //将该切面类和目标类放到map集合中
                    aspectMap.put(classBySuper, targetClassSet);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        //加上事务代理TransactionProxy所对应的所有Service类
        addTransactionProxy(aspectMap);
        return aspectMap;
    }
    /**
     *  获取事务切面类-目标类集合的映射
     */
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> aspectMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        aspectMap.put(TransactionProxy.class, serviceClassSet);
    }
    /**
     * 根据@Aspect定义的包名和类名去获取对应的目标类集合
     * 这里将Spring中的Aspect 与 cutPoint 融合在一起组成的自定义Aspect
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> result = new HashSet<>();
        //包名
        String pkg = aspect.pkg();
        //类名
        String cls = aspect.cls();

        if (!pkg.equals("") && !cls.equals("")) {
            //如果包名和类名都不为空，添加指定类
            result.add(Class.forName(pkg + "." + cls));
        } else if (!pkg.equals("")) {
            //如果包名不为空，类名为空。则添加该包名下所有类
            result.addAll(ClassUtil.getClassSet(pkg));

        }

        return result;
    }

    /**
     * 将切面类-目标类集合的映射关系 转化为 目标类-切面对象列表的映射关系
     * （因为有多个切面类 对于 一个目标类的情况）
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> aspectMap) throws Exception {
        Map<Class<?>, List<Proxy>> result =new HashMap<>();

        for (Map.Entry<Class<?>, Set<Class<?>>> classSetEntry : aspectMap.entrySet()) {
            //切面类
            Class<?> aspectClass  = classSetEntry.getKey();
            //目标类集合
            Set<Class<?>> targetClassSet = classSetEntry.getValue();
            //切面对象
            Proxy proxy = (Proxy)aspectClass.newInstance();
            for (Class<?> targetClass : targetClassSet) {
                if(result.containsKey(targetClass)){
                    //如果原Map集合中就有这个目标类KEY，在KEY的list中添加当前切面对象
                    result.get(targetClass).add(proxy);
                }else{
                    //如果原Map集合中没有这个目标类KEY，创建新的映射，并添加
                    List<Proxy> list =new ArrayList<>();
                    list.add(proxy);
                    result.put(targetClass,list);
                }
            }
        }
        return result;
    }
}
