package com.mvc.framework.bean;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * Param类用于封装Controller方法的参数.
 * @author study
 * @create 2019-11-09 14:45
 */
public class Param {
    private Map<String, Object> paramMap;

    public Param() {
    }

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public boolean isEmpty(){
        return MapUtils.isEmpty(paramMap);
    }


}
