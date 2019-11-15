package com.mvc.framework.annotation;

import java.lang.annotation.*;

/**
 * 事务注解
 * @author study
 * @create 2019-11-11 21:13
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

}
