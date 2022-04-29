package com.demo.common.service;

import com.demo.common.entity.User;


public interface UserService {
    User getUserByUserId(Integer id);

    Integer insertUserId(User user);
}
