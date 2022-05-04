package com.demo.server;

import com.demo.common.utils.ReflectionUtils;
import com.demo.proto.Request;
import com.demo.proto.Response;
import com.demo.proto.ServiceDescriptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author: shf
 * @description: BIO处理的工作线程
 * @date: 2022/5/3 14:49
 */

@Slf4j
@AllArgsConstructor
public class WorkThread implements Runnable{
    private Socket socket;
    private ServiceManager serviceManager;

    @Override
    public void run() {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 读取客户端传过来的request
            Request request = (Request) ois.readObject();
            Response response = getResp(request);
            //写入到客户端
            oos.writeObject(response);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.warn(e.getMessage(), e);
        }

    }

    private Response getResp(Request request) {
        ServiceInstance instance = serviceManager.lookup(request);
        Object invoke = ReflectionUtils.invoke(
                instance.getTarget(),
                instance.getMethod(),
                request.getParameters());
        return Response.success(invoke, request.getService().getReturnType());
    }
}
