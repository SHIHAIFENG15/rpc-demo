package com.demo.server;


import com.alibaba.fastjson.JSON;
import com.demo.codec.Decoder;
import com.demo.codec.Encoder;
import com.demo.common.utils.ReflectionUtils;
import com.demo.proto.Request;
import com.demo.proto.Response;
import com.demo.transport.RequestHandler;
import com.demo.transport.TransportServer;
import lombok.extern.slf4j.Slf4j;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author: shf
 * @description:
 * @date: 2022/4/29 15:21
 */

@Slf4j
public class RpcServer {
    private RpcServerConfig config;
    private Encoder encoder;
    private Decoder decoder;
    private TransportServer net;
    private ServiceManager manager;

    public RpcServer(){
        this(new RpcServerConfig());
    }

    public RpcServer(RpcServerConfig config) {
        this.config = config;

        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        net.init(config.getPort(), handler);

        this.manager = new ServiceManager();
    }

    public <T> void register(Class<T> interfaceClass, T bean) {
        manager.register(interfaceClass, bean);
    }

    public void start() {
        this.net.start();
    }

    public void stop() {
        this.net.stop();
    }

    private RequestHandler handler = new RequestHandler() {

        @Override
        public void onRequest(InputStream receive, OutputStream toResp) {
            Response resp = null;

            try {
                byte[] inBytes = IOUtils.readNBytes(receive, receive.available());
                Request request = decoder.decode(inBytes, Request.class);

                log.info("Server receive request, in process....");
                ServiceInstance instance = manager.lookup(request);
                Object[] objs = request.getParameters();
                for (int i = 0; i < objs.length; i++) {
                    objs[i] = JSON.parseObject(objs[i].toString(), Class.forName(request.getService().getParameterTypes()[i]));
                }
                Object o = ReflectionUtils.invoke(instance.getTarget(), instance.getMethod(), request.getParameters());
                resp = Response.success(o);
            } catch (IOException | ClassNotFoundException e) {
                log.error(e.getMessage(), e);
                resp = Response.fail("RpcServer got error: "
                        + e.getClass().getName()
                        + ": " + e.getMessage());
            } finally {
                byte[] bytes = encoder.encode(resp);
                try {
                    toResp.write(bytes);
                    log.info("Write response back...");
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    };
}
