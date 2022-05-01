package com.demo.client;

import com.demo.codec.Decoder;
import com.demo.codec.Encoder;
import com.demo.common.entity.User;
import com.demo.common.service.UserService;
import com.demo.common.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.Random;

/**
 * @author: shf
 * @description:
 * @date: 2022/4/29 15:01
 */

@Slf4j
public class RpcClient {
    private RpcClientConfig config;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RpcClient() {
        this(new RpcClientConfig());
    }

    public RpcClient(RpcClientConfig config) {
        this.config = config;

        // 根据config确定各组件的实例类型
        this.encoder = ReflectionUtils.newInstance(config.getEncodeClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());
        this.selector = ReflectionUtils.newInstance(config.getSelectorClass());
        this.selector.init(
                this.config.getServers(),
                this.config.getConnectCount(),
                this.config.getTransportClass()
        );
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                new RemoteInvoker(clazz, encoder, decoder, selector)
        );
    }
}
