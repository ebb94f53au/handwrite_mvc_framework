package com.siyang.service;

import com.mvc.framework.annotation.Service;
import com.siyang.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author study
 * @create 2019-11-07 21:50
 */

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User getOneUser(int id) {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "Tom", 22));
        userList.add(new User(2, "Alic", 12));
        userList.add(new User(3, "Bob", 32));
        return userList.get(id);
    }

    /**
     * 获取所有用户
     */
    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "Tom", 22));
        userList.add(new User(2, "Alic", 12));
        userList.add(new User(3, "Bob", 32));
        return userList;
    }
}
