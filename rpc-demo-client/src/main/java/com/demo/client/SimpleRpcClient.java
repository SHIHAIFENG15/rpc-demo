package com.demo.client;

import com.demo.proto.Request;
import com.demo.proto.Response;
import com.demo.server.register.ServiceRegister;
import com.demo.server.register.ZkServiceRegister;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author: shf
 * @description:
 * @date: 2022/4/29 15:01
 */

@Slf4j

public class SimpleRpcClient implements RpcClient{
    private String host;
    private int port;
    private ServiceRegister serviceRegister;

    // 加入注册中心之后，服务端结点选择不需要客户端传入
//    public SimpleRpcClient(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }

    public SimpleRpcClient() {
        // 初始化注册中心，建立连接
        this.serviceRegister = new ZkServiceRegister();
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                new RemoteInvoker(clazz, hanlder)
        );
    }

    private RemoteHandler hanlder = new RemoteHandler() {
        @Override
        public Response getResp(Request request) {
            Socket socket = null;
            // 从注册中心获取host，port
            InetSocketAddress address = serviceRegister.serviceDiscovery(request.getService());
            host = address.getHostName();
            port = address.getPort();

            try {
                socket = new Socket(host, port);

                // 写入数据,序列化交给ObjectOutputStream,传送request
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(request);
                outputStream.flush();

                // 读回数据
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                return (Response) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                log.warn(e.getMessage(), e);
                return Response.fail("An error occurred in Socket Client: " + e.getMessage());
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
    };
}
