package com.demo.client;

import com.demo.common.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * @author: shf
 * @description:
 * @date: 2022/4/29 15:01
 */

@Slf4j
public class RpcClient {
    public static void main(String[] args) {
        Socket socket = null;

        try {
            socket = new Socket("127.0.0.1", 8000);

            // 写入数据,序列化交给ObjectOutputStream
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            int id= new Random().nextInt();
            outputStream.writeInt(id);
            log.info("客户端查询id为： " + id + "的对象");
            outputStream.flush();

            // 读回数据
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            User user = (User) inputStream.readObject();
            log.info("得到了远程对象： " + user);
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
