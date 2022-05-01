package com.demo.common.service;

import com.demo.common.entity.User;

/*
* 注意需要
* */
public interface UserService {
    User getUserByUserId(Integer id);

    Integer insertUser(User user);
}
