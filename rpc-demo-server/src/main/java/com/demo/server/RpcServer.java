package com.demo.server;

import com.demo.common.entity.User;
import com.demo.common.service.UserService;
import com.demo.common.service.UserServiceImpl;
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
        UserService userService = new UserServiceImpl();

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8000);
            log.info("Server listening...");

            while (true) {
                Socket socket = serverSocket.accept();

                new Thread(() -> {
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        int id = inputStream.readInt();
                        User user = userService.getUserByUserId(id);

                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        outputStream.writeObject(user);
                        outputStream.flush();
                    } catch (IOException e) {
                        log.error(e.getMessage() + e);
                    }
                }).start();
            }

        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
