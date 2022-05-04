package com.demo.example;

import com.demo.common.service.UserService;
import com.demo.common.service.UserServiceImpl;
import com.demo.server.NettyServer;
import com.demo.server.RpcServer;
import com.demo.server.ServiceManager;
import com.demo.server.SimpleRpcServer;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/3 17:02
 */

public class TestServer {
    public static void main(String[] args) {
        RpcServer service1 = new NettyServer(new ServiceManager("127.0.0.1", 8000));
        RpcServer service2 = new SimpleRpcServer(new ServiceManager("101.35.220.233", 3000));
//        RpcServer service = new SimpleRpcServer(new ServiceManager());
        service1.register(UserService.class, new UserServiceImpl());
        service1.start(8000);
        service2.register(UserService.class, new UserServiceImpl());
        service2.start(3000);
    }
}
