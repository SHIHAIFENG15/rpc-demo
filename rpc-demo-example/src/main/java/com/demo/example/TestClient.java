package com.demo.example;

import com.demo.client.NettyRpcClient;
import com.demo.client.RpcClient;
import com.demo.client.SimpleRpcClient;
import com.demo.common.entity.User;
import com.demo.common.service.UserService;

import java.util.Random;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/3 17:01
 */

public class TestClient {
    public static void main(String[] args) {
        RpcClient rpcClient = new NettyRpcClient();
//        RpcClient rpcClient = new SimpleRpcClient("127.0.0.1", 8000);
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
