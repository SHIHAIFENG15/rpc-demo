package com.demo.transport;

/**
 * @author: shf
 * @description: 1.初始化，监听端口 2.接收请求 3.关闭
 * @date: 2022/5/1 15:55
 */

public interface TransportServer {
    void init(int port, RequestHandler handler);

    void start();

    void stop();
}
