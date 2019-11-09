package com.mvc.framework.annotation;

import com.mvc.framework.bean.RequestMethod;

import java.lang.annotation.*;

/**
 * @author study
 * @create 2019-11-06 20:25
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    /**
     * 请求接口路径
     * @return
     */
    String value();

    /**
     * 请求方法
     * @return
     */
    RequestMethod method() default RequestMethod.GET;

}

