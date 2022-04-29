package com.demo.client;

import com.demo.common.entity.User;
import com.demo.common.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.Random;

/**
 * @author: shf
 * @description:
 * @date: 2022/4/29 15:01
 */

@Slf4j
public class RpcClient {
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                new RemoteInvoker(clazz)
        );
    }

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient();
        UserService userService = rpcClient.getProxy(UserService.class);

        User user = userService.getUserByUserId(new Random().nextInt());
        System.out.println("得到用户： " + user);

        System.out.println(userService.insertUserId(User.builder()
                                .userName("shf")
                                .gender(true)
                                .id(1)
                                .build()));
    }
}
