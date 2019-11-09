package com.mvc.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 将应用中所有的Class对象都存储到一个集合中
 * @author study
 * @create 2019-11-06 21:27
 */
public final class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader(){
        //xx.class.getClassLoader().getResourceAsStream
        //可能会导致和当前线程所运行的类加载器不一致。所以一般使用这个
        return  Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * name:类的全限定名，如：com.org.prj
     * initialize:如果为true，则会在返回Class对象之前，对该类型做连接，校验，初始化操作。(如：执行static块中的代码)，initialize默认需要初始化。
     * loader:用自定义的类加载器来请求这个类型；当然，你也可以传入null，用bootstrap加载器
     * 由于Class.forName默认是需要初始化，一旦初始化，就会触发目标对象的 static块代码执行，static参数也也会被再次初始化
     * @param className 类名
     * @param isInitialized 是否初始化
     * @return
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure",e);
        }

        return aClass;
    }

    /**
     * 加载类（默认将初始化类）
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className,true);
    }
    /**
     * 获取指定包名下的所有类
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<>();
        try {
            Optional<URL> resource = Optional.ofNullable(getClassLoader().getResource(packageName.replace(".", "/")));
            resource.ifPresent(url->{
                //如果是文件就遍历
                if(url.getProtocol().equals("file")){

                    addClass(classSet,url.getPath(),packageName);
                }else if(url.getProtocol().equals("jar")){
                    System.out.println(url.getPath());
//                    try {
//                        JarURLConnection urlConnection = (JarURLConnection)url.openConnection();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return classSet;
    }

    /**
     * 遍历路径
     * @param classSet
     * @param packageName
     */
    private static void addClass(Set<Class<?>> classSet,String packagePath,String packageName) {
        //过滤文件
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(".class") || file.isDirectory();
            }
        });

        Optional.ofNullable(files).ifPresent(file->{
            for (File f : file) {
                if(f.isDirectory()){
                    //如果是文件夹递归遍历
                    addClass(classSet,packagePath+"/"+f.getName(),packageName+"."+f.getName());
                }else{
                    //如果是class文件，直接加入set
                    doAddClass(classSet,packageName+"."+f.getName().split("\\.")[0]);
                }
            }
        });



    }

    /**
     * 根据类路径将类实例化添加到classSet集合中
     * @param classSet
     * @param className
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> aClass = loadClass(className);
        classSet.add(aClass);
    }
}
