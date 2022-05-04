package com.demo.client;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/3 16:02
 */

public interface RpcClient {
    public <T> T getProxy(Class<T> clazz);
}
