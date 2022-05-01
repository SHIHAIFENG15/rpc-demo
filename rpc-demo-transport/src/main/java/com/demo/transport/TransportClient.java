package com.demo.transport;

import com.demo.proto.Peer;

import java.io.InputStream;

/**
 * @author: shf
 * @description: 1.创建连接 2.发送数据，并等待响应 3.关闭连接
 * @date: 2022/5/1 11:11
 */

public interface TransportClient {
    void connect(Peer peer);

    InputStream write(InputStream data);

    void close();
}
