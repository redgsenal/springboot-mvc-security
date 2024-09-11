package com.springboot.tutorial.mvc.security.dao;

import com.springboot.tutorial.mvc.security.entity.User;

public interface UserDao {
    User findByUserName(String userName);
}
