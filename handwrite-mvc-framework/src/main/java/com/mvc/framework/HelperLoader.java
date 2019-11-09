package com.mvc.framework;

import com.mvc.framework.helper.BeanHelper;
import com.mvc.framework.helper.ClassHelper;
import com.mvc.framework.helper.ConfigHelper;
import com.mvc.framework.helper.IocHelper;
import com.mvc.framework.util.ClassUtil;

/**
 * 加载4个helper
 * 到目前为止, 我们创建了ClassHelper, BeanHelper, IocHelper, ControllerHelper这四个Helper类,
 * 我们需要一个入口程序来加载他们(实际上是加载静态代码块),
 * 当然就算没有这个入口程序, 这些类也会被加载, 我们这里只是为了让加载更加集中.
 * @author study
 * @create 2019-11-09 15:00
 */
public class HelperLoader {

    public static void init(){
        Class<?>[] classList={
            ConfigHelper.class,
                ClassHelper.class,
                    BeanHelper.class,
                        IocHelper.class
        };
        for (Class<?> aClass : classList) {
            ClassUtil.loadClass(aClass.getName());
        }

    }
}
