package com.demo.example;

import com.demo.client.RpcClient;
import com.demo.common.entity.User;
import com.demo.common.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author: shf
 * @description: 测试Client类
 * @date: 2022/5/1 17:00
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        UserService service = client.getProxy(UserService.class);

        User shf = User.builder()
                .gender(true)
                .userName("shf")
                .id(1)
                .build();
        service.insertUser(shf);
        log.info("插入一位用户： " + shf);

        User user = service.getUserByUserId(new Random().nextInt());
        log.info("请求到一位用户： " + user);
    }
}
