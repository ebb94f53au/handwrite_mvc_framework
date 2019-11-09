package com.mvc.framework.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View类用于封装Controller方法的视图返回结果.
 * @author study
 * @create 2019-11-09 14:49
 */
public class View {
    /**
     * 视图路径
     */
    private String path;

    /**
     * 模型数据
     */
    private Map<String, Object> model;

    public View(String path) {
        this.path = path;
        model = new HashMap<String, Object>();
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }


}
