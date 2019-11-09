package com.mvc.framework.bean;

/**
 * Data类用于封装Controller方法的JSON返回结果.
 * @author study
 * @create 2019-11-09 14:49
 */
public class Data {
    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
