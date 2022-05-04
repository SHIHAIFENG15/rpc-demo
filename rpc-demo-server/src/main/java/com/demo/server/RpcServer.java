package com.demo.server;

public interface RpcServer {
    public <T> void register(Class<T> interfaceClass, T bean);

    void start(int port);

    void stop();
}
