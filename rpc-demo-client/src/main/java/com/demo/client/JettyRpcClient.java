package com.demo.client;

import com.demo.proto.Request;
import com.demo.proto.Response;

import java.lang.reflect.Proxy;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/3 17:06
 */

//public class JettyRpcClient implements RpcClient{
//    private String host;
//    private int port;
//
//    public JettyRpcClient(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }
//
//    public <T> T getProxy(Class<T> clazz) {
//        return (T) Proxy.newProxyInstance(
//                getClass().getClassLoader(),
//                new Class[]{clazz},
//                new RemoteInvoker(clazz, hanlder)
//        );
//    }
//
//    private RemoteHandler hanlder = new RemoteHandler() {
//        @Override
//        public Response getResp(Request request) {
//
//        }
//    };
//}
