package com.demo.server;

import com.demo.common.entity.User;
import com.demo.common.service.UserService;
import com.demo.common.service.UserServiceImpl;
import com.demo.common.utils.ReflectionUtils;
import com.demo.proto.Request;
import com.demo.proto.Response;
import com.demo.proto.ServiceDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: shf
 * @description: BIO + Sockect的Server
 * @date: 2022/4/29 15:21
 */

@Slf4j
public class SimpleRpcServer implements RpcServer{
    private ServiceManager serviceManager;

    public SimpleRpcServer(ServiceManager manager) {
        this.serviceManager = manager;
    }

    @Override
    public <T> void register(Class<T> interfaceClass, T bean) {
        serviceManager.register(interfaceClass, bean);
    }

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            log.info("Server listening...");
            // BIO的方式监听Socket
            while (true){
                Socket socket = serverSocket.accept();
                // 开启一个新线程去处理
                new Thread(new WorkThread(socket, serviceManager)).start();
            }
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void stop() {

    }
}
