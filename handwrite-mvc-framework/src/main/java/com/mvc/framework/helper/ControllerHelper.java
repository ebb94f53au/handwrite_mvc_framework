package com.mvc.framework.helper;

import com.mvc.framework.annotation.RequestMapping;
import com.mvc.framework.bean.Handler;
import com.mvc.framework.bean.Request;
import com.mvc.framework.bean.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ControllerHelper 助手类定义了一个"请求-处理器" 的映射 REQUEST_MAP,
 * @author study
 * @create 2019-11-09 14:14
 */
public class ControllerHelper {
    /**
     * REQUEST_MAP 就相当于Spring MVC里的映射处理器, 接收到请求后返回对应的处理器.
     */
    private static final Map<Request, Handler> REQUEST_HANDLER_MAP = new HashMap<Request, Handler>();

    /**
     * REQUEST_MAP 映射处理器的实现逻辑如下:
     *
     * 首先通过 ClassHelper 工具类获取到应用中所有Controller的Class对象,
     * 然后遍历Controller及其所有方法, 将所有带 @RequestMapping 注解的方法封装为处理器,
     * 将 @RequestMapping 注解里的请求路径和请求方法封装成请求对象, 然后存入 REQUEST_MAP 中.
     */
    static {
        //获的controller的class对象
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        //遍历Controller
        controllerClassSet.forEach(controllerClass->{
            String root ="";
            //先判断类上是否有RequestMapping注解
            if(controllerClass.isAnnotationPresent(RequestMapping.class)){
                //给根路径赋值
                root = controllerClass.getAnnotation(RequestMapping.class).value();
            }
            Method[] declaredMethods = controllerClass.getDeclaredMethods();
            //遍历类上的所有方法
            for (Method declaredMethod : declaredMethods) {
                //再判断方法上是否有RequestMapping注解
                if(declaredMethod.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
                    //得到请求路径和请求方法
                    String url = annotation.value();
                    String method = annotation.method().name();
                    //将 @RequestMapping 注解里的请求路径和请求方法封装成请求对象, 然后存入 REQUEST_MAP 中.
                    REQUEST_HANDLER_MAP.put(new Request(root+url,method),new Handler(controllerClass,declaredMethod));

                }

            }

        });


    }

    /**
     * 根据Request查找请求处理映射器，返回handler
     * @param request
     * @return
     */
   public static Handler getHandler(Request request){
        return REQUEST_HANDLER_MAP.get(request);
   }

    /**
     * 根据requestPath，requestMethod查找请求处理映射器，返回handler
     * @param requestPath
     * @param requestMethod
     * @return
     */
   public static Handler getHandler(String requestPath,String requestMethod){

        return REQUEST_HANDLER_MAP.get(new Request(requestPath,requestMethod));
   }

}
