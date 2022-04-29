package com.demo.client;

import com.demo.common.entity.User;
import com.demo.proto.Request;
import com.demo.proto.Response;
import com.demo.proto.ServiceDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Random;

/**
 * @author: shf
 * @description: 返回远程服务的代理类
 * @date: 2022/4/29 21:41
 */

@Slf4j
public class RemoteInvoker implements InvocationHandler{
    private Class clazz;

    public RemoteInvoker(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);

        Response response = invokeRemote(request);
        if (response == null || response.getCode() != 200) {
            throw new IllegalStateException("Fail to invoke remote: " + request);
        }

        return response.getData();
    }

    public Response invokeRemote(Request request) {
        Socket socket = null;

        try {
            socket = new Socket("127.0.0.1", 8000);

            // 写入数据,序列化交给ObjectOutputStream,传送request
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);
            outputStream.flush();

            // 读回数据
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return (Response) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return Response.fail();
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
