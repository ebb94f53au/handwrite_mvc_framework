package com.mvc.framework.annotation;

import java.lang.annotation.*;

/**
 * 切面注解
 * 这里将Spring中的Aspect 与 cutPoint 融合在一起组成的自定义Aspect
 * @author study
 * @create 2019-11-11 21:12
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    /**
     * 包名
     */
    String pkg() default "";

    /**
     * 类名
     */
    String cls() default "";

}
