package com.demo.common.service;

import com.demo.common.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;

/**
 * @author: shf
 * @description:
 * @date: 2022/4/29 14:52
 */

@Slf4j
public class UserServiceImpl implements UserService{
    // 模拟从数据库中取用户的行为,打日志观察动作
    public User getUserByUserId(Integer id) {
        Random random = new Random();
        User user = User.builder()
                .userName(UUID.randomUUID().toString())
                .id(id)
                .gender(random.nextBoolean()).build();
        log.info("服务结点从该服务请求数据...");
        return user;
    }

    @Override
    public Integer insertUser(User user) {
        log.info("服务结点从该服务插入数据: " + user);
        return 1;
    }
}
