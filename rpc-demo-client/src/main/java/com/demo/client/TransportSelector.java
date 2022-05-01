package com.demo.client;

import com.demo.proto.Peer;
import com.demo.transport.TransportClient;

import java.util.List;

/**
 * @author: shf
 * @description: selector接口
 * @date: 2022/5/1 12:10
 */

public interface TransportSelector {
    /*
     * @param peers: 可以连接的server端点信息
     * @param count: client与server建立连接数
     * @param clazz: client实现的class
     * @description 初始化selector
     * @date 2022/5/1 12:15
     */
    void init(List<Peer> peers, int count,
              Class<? extends TransportClient> clazz);

    /*
    选择一个transport与server做交互
     */
    TransportClient select();

    /*
    释放用完的client
     */
    void release(TransportClient client);

    void close();
}
