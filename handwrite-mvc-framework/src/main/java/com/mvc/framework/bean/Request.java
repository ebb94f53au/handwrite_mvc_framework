package com.mvc.framework.bean;

import java.util.Objects;

/**
 * 请求类中的方法和路径对应 @RequestMapping 注解里的方法和路径.
 * @author study
 * @create 2019-11-09 13:49
 */
public class Request {
    /**
     * 请求路径
     */
    private String requestPath;
    /**
     * 请求方式
     */
    private String requestMethod;

    public Request(String requestPath, String requestMethod) {
        this.requestPath = requestPath;
        this.requestMethod = requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return requestPath.equals(request.requestPath) &&
                requestMethod.equals(request.requestMethod);
    }

    @Override
    public int hashCode() {
        int result= 17;
        result= 31*result+requestPath.hashCode();
        result= 31*result+requestMethod.hashCode();
        return result;
    }
}
