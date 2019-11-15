package com.test;

import com.test.bean.*;
import net.sf.cglib.proxy.Enhancer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author study
 * @create 2019-11-11 12:57
 */
public class Test01 {

    @Test
    public void demo1 (){
        IAccountService target =  new IAccountServiceImpl();
        IAccountService o = (IAccountService)Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new AccountAdvice(target)
        );
        o.transfer();

    }
    @Test
    public void demo2 (){
        //创建目标对象
        IAccountService o = (IAccountService)Enhancer.create(IAccountServiceImpl.class, new AccountAdvice2());
        o.transfer();
    }
    @Test
    public void demo3 () throws Exception{
        Class<?> dao = Class.forName("com.test.bean.UserDao");
        Class<?> service = Class.forName("com.test.bean.UserService");
        Map<Class<?>,Object> map =new HashMap<>();
        map.put(dao,dao.newInstance());
        map.put(service,service.newInstance());

        Field dao1 = service.getDeclaredField("dao");
        dao1.setAccessible(true);
        dao1.set(map.get(service),map.get(dao));

        ((UserService)map.get(service)).todo();
        map.put(dao,new UserDao("12"));
        ((UserService)map.get(service)).todo();
    }
    @Test
    public void demo4 () throws Exception{
        Class<?> entityClass=Class.forName("com.test.Test01");
        Map<String,Object> fieldMap=new HashMap<>();
        fieldMap.put("ss",new Test01());
        fieldMap.put("s2",new Test01());
        String columns ="";
        String values ="";
        for (Map.Entry<String, Object> stringObject : fieldMap.entrySet()) {
            columns+= stringObject.getKey()+",";
            values+= "?,";
        }
        columns = columns.substring(0,columns.length()-1);
        values = values.substring(0,values.length()-1);
        String sql = "INSERT INTO " + entityClass.getSimpleName() +"("+columns+") VALUES ("+values+")" ;
        System.out.println(sql);
    }


}
