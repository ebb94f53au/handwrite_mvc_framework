package com.mvc.framework.helper;

import com.mvc.framework.bean.Param;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 前端控制器接收到HTTP请求后, 从HTTP中获取请求参数, 然后封装到Param对象中.
 * @author study
 * @create 2019-11-09 14:51
 */
public class RequestHelper {
    /**
     * 获取请求参数
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        Map<String, Object> paramMap =new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        //如果没有参数
        if(!parameterNames.hasMoreElements()){
            //这里传入一个空构造器
            return new Param();
        }
        //获取参数
        while(parameterNames.hasMoreElements()){
            String s = parameterNames.nextElement();
            String parameter = request.getParameter(s);
            paramMap.put(s,parameter);
        }
        return new Param(paramMap);
    }

}
