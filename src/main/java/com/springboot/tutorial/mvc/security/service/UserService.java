package com.springboot.tutorial.mvc.security.service;

import com.springboot.tutorial.mvc.security.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    public User findByUserName(String userName);

}