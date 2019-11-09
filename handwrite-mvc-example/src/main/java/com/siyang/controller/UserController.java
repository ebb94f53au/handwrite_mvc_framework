package com.siyang.controller;

import com.mvc.framework.annotation.Autowired;
import com.mvc.framework.annotation.Controller;
import com.mvc.framework.annotation.RequestMapping;
import com.mvc.framework.bean.Data;
import com.mvc.framework.bean.Param;
import com.mvc.framework.bean.RequestMethod;
import com.mvc.framework.bean.View;
import com.siyang.bean.User;
import com.siyang.service.UserService;

import java.util.Arrays;
import java.util.Map;

/**
 * @author study
 * @create 2019-11-07 21:50
 */
@Controller
@RequestMapping(value = "/user",method = RequestMethod.DELETE)
public class UserController {
    @Autowired
    UserService userService;
    @RequestMapping(value = "/getAll")
    public Data getAll (){

       return  new Data(userService.getAllUser());
    }
    @RequestMapping(value = "/getAllView")
    public View getAllView (){
        View index = new View("index.jsp");
        index.addModel("userList",userService.getAllUser());
       return index ;
    }
    @RequestMapping(value = "/getOne")
    public View getOne (Param p){
        Map<String, Object> paramMap = p.getParamMap();
        //需要将id 转换为 string 然后转化为 int
        int key =0;
        try {

            key = Integer.parseInt(paramMap.get("id").toString()) ;
        }catch (Exception e){

        }

        View index = new View("index.jsp");
        User oneUser = userService.getOneUser(key);

        index.addModel("userList", Arrays.asList(oneUser));
        return index ;
    }
}
