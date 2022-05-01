package com.demo.transport;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author: shf
 * @description: 该接口包含服务器的具体处理动作，在网络模块中不做实现，是一次解耦
 * @date: 2022/5/1 15:57
 */

public interface RequestHandler {
    void onRequest(InputStream receive, OutputStream toResp);
}