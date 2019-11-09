package com.mvc.framework;

import com.alibaba.fastjson.JSON;
import com.mvc.framework.bean.Data;
import com.mvc.framework.bean.Handler;
import com.mvc.framework.bean.Param;
import com.mvc.framework.bean.View;
import com.mvc.framework.helper.BeanHelper;
import com.mvc.framework.helper.ConfigHelper;
import com.mvc.framework.helper.ControllerHelper;
import com.mvc.framework.helper.RequestHelper;
import com.mvc.framework.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 实现前端控制器
 * 前端控制器实际上是一个Servlet, 这里配置的是拦截所有请求, 在服务器启动时实例化.
 *
 * 当DispatcherServlet实例化时, 首先执行 init() 方法,
 * 这时会调用 HelperLoader.init() 方法来加载相关的helper类, 并注册处理相应资源的Servlet.
 *
 * 对于每一次客户端请求都会执行 service() 方法, 这时会首先将请求方法和请求路径封装为Request对象,
 * 然后从映射处理器 (REQUEST_MAP) 中获取到处理器. 然后从客户端请求中获取到Param参数对象, 执行处理器方法.
 * 最后判断处理器方法的返回值, 若为view类型, 则跳转到jsp页面, 若为data类型, 则返回json数据.
 * @author study
 * @create 2019-11-09 15:03
 */
//@WebServlet(urlPatterns ="/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        //加载4个helper

        HelperLoader.init();
        //注册处理jsp和静态资源的servlet
        registerServlet(config.getServletContext());
    }
    /**
     * DefaultServlet和JspServlet都是由Web容器创建
     * org.apache.catalina.servlets.DefaultServlet
     * org.apache.jasper.servlet.JspServlet
     */
    private void registerServlet(ServletContext servletContext) {
        //Tomcat /conf/web.xml 已经配置好
        //通过 getServletRegistration 直接搜索servlet名找到该servlet 并且映射url

        //动态注册处理jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        // DEMO:/WEB-INF/view/
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");

        //动态注册 处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("/favicon.ico"); //网站头像
        // DEMO:/asset/
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");


    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.得到请求方法和请求路径
        String requestMethod = req.getMethod().toUpperCase();
        String contextPath = req.getContextPath();
        String requestPath = req.getRequestURI().replace(contextPath, "");

        //2.根据请求方法和请求路径得到handler
        Handler handler = ControllerHelper.getHandler(requestPath, requestMethod);
        if(handler!=null){
            //3.得到请求的参数
            Param param = RequestHelper.createParam(req);
            Method method = handler.getControllerMethod();
            Object controller = BeanHelper.getBean(handler.getControllerClass());
            //4.执行方法
            Object result;
            if(param.isEmpty()){
                //参数为空
                result = ReflectionUtil.invokeMethod(controller, method);
            }else {
                //参数不为空
                result = ReflectionUtil.invokeMethod(controller, method, param);

            }

            //5.根据返回值的不同，跳转到页面或者返回json
            if(result instanceof View){
                handleViewResult((View)result,req,resp);
            }else if(result instanceof Data){
                handleDataResult((Data) result,resp);
            }
        }

    }

    /**
     * 跳转页面
     */
    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        Map<String, Object> model = view.getModel();
        if (!path.isEmpty()) {
            if(path.startsWith("/")){//重定向
                response.sendRedirect(path);
            }else{//请求转发
                //将数据发送给转发端
                for (Map.Entry<String, Object> ob : model.entrySet()) {
                    request.setAttribute(ob.getKey(),ob.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);
            }
        }

    }

    /**
     * 返回JSON数据
     */
    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if(model!=null){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            String json = JSON.toJSONString(model);
            printWriter.write(json);
            printWriter.flush();
            printWriter.close();
        }

    }

}
