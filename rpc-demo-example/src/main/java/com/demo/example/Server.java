package com.demo.example;

import com.demo.common.service.UserService;
import com.demo.common.service.UserServiceImpl;
import com.demo.server.RpcServer;

/**
 * @author: shf
 * @description: 测试Server
 * @date: 2022/5/1 16:58
 */

public class Server {
    public static void main(String[] args) {
        RpcServer server = new RpcServer();
        server.register(UserService.class, new UserServiceImpl());
        server.start();
    }
}
