package com.mvc.framework;

/**
 * 维护配置文件中相关的配置项名称
 * @author study
 * @create 2019-11-06 20:37
 */
public interface ConfigConstant {
    //配置文件的名称
    String CONFIG_FILE = "handwrite.properties";

    //数据源
    String JDBC_DRIVER = "handwrite.framework.jdbc.driver";
    String JDBC_URL = "handwrite.framework.jdbc.url";
    String JDBC_USERNAME = "handwrite.framework.jdbc.username";
    String JDBC_PASSWORD = "handwrite.framework.jdbc.password";

    //java源码地址
    String APP_BASE_PACKAGE = "handwrite.framework.app.base_package";
    //jsp页面路径
    String APP_JSP_PATH = "handwrite.framework.app.jsp_path";
    //静态资源路径
    String APP_ASSET_PATH = "handwrite.framework.app.asset_path";

}
