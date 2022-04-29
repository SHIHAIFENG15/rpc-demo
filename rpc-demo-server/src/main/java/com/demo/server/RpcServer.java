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
 * @description:
 * @date: 2022/4/29 15:21
 */

@Slf4j
public class RpcServer {
    public static void main(String[] args) {
        ServiceManager sm = new ServiceManager();
        sm.register(UserService.class, new UserServiceImpl());

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8000);
            log.info("Server listening...");

            while (true) {
                Socket socket = serverSocket.accept();

                new Thread(() -> {
                    Response resp = new Response();
                    try {
                        // 先读取request
                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        Request request = (Request) inputStream.readObject();

                        // 查找服务，代理，提取对象，包装成Response写回
                        ServiceInstance instance = sm.lookup(request);
                        Object o = ReflectionUtils.invoke(instance.getTarget(), instance.getMethod(), request.getParameters());
                        resp = Response.success(o);

                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        outputStream.writeObject(resp);
                        outputStream.flush();
                    } catch (IOException | ClassNotFoundException e) {
                        log.error(e.getMessage() + e);
                    }
                }).start();
            }

        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
