package com.siyang.service;

import com.siyang.bean.User;

import java.util.List;

/**
 * @author study
 * @create 2019-11-07 23:06
 */
public interface UserService {
    public List<User> getAllUser();
    public User getOneUser( int id);
}
