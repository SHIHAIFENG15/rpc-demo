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
import java.rmi.Remote;
import java.util.Random;

/**
 * @author: shf
 * @description: 返回远程服务的代理类
 * @date: 2022/4/29 21:41
 */

@Slf4j
public class RemoteInvoker implements InvocationHandler{
    private Class clazz;
    private RemoteHandler handler;

    public RemoteInvoker(Class clazz, RemoteHandler handler) {
        this.clazz = clazz;
        this.handler = handler;
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

    // 网络逻辑放到Client实现handler,遵循开闭原则
    public Response invokeRemote(Request request) {

        if (handler != null) {
            return handler.getResp(request);
        } else {
            throw new IllegalStateException("Remote handler is empty : " + request);
        }
    }
}
