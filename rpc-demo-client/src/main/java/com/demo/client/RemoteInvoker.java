package com.demo.client;

import com.alibaba.fastjson.JSON;
import com.demo.codec.Decoder;
import com.demo.codec.Encoder;
import com.demo.proto.Request;
import com.demo.proto.Response;
import com.demo.proto.ServiceDescriptor;
import com.demo.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import sun.misc.IOUtils;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: shf
 * @description: 返回远程服务的代理类
 * @date: 2022/4/29 21:41
 */

@Slf4j
public class RemoteInvoker implements InvocationHandler{
    private Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RemoteInvoker(Class clazz, Encoder encoder, Decoder decoder, TransportSelector selector) {
        this.clazz = clazz;
        this.encoder = encoder;
        this.decoder = decoder;
        this.selector = selector;
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

        // 返回值需要进一步类型转换
        // return response.getData();
        return JSON.parseObject(response.getData().toString(), Class.forName(request.getService().getReturnType()));
    }

    public Response invokeRemote(Request request) {
        TransportClient client = null;
        Response resp = null;

        try {
            client = selector.select();

            byte[] inBytes = encoder.encode(request);
            InputStream inputStream = client.write(new ByteArrayInputStream(inBytes));

            byte[] outBytes = IOUtils.readNBytes(inputStream, inputStream.available());
            resp = decoder.decode(outBytes, Response.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Response.fail("RpcClient got error: "
                    + e.getClass()
                    + ": " +e.getMessage());
        } finally {
            if (client != null) {
                selector.release(client);
            }
        }

        return resp;
    }
}
