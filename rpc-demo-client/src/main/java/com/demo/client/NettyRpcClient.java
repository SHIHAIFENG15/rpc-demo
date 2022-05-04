package com.demo.client;

import com.demo.proto.Request;
import com.demo.proto.Response;
import com.demo.server.register.ServiceRegister;
import com.demo.server.register.ZkServiceRegister;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/3 16:33
 */

@Slf4j
public class NettyRpcClient implements RpcClient{
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private ServiceRegister serviceRegister;
    private String host;
    private int port;
    public NettyRpcClient() {
        // 初始化注册中心，建立连接
        this.serviceRegister = new ZkServiceRegister();
    }
    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    @Override
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                new RemoteInvoker(clazz, hanlder)
        );
    }

    private RemoteHandler hanlder = new RemoteHandler() {

        @Override
        public Response getResp(Request request) {
            Response response = null;
            // 从注册中心获取host，port
            InetSocketAddress address = serviceRegister.serviceDiscovery(request.getService());
            host = address.getHostName();
            port = address.getPort();

            try {
                ChannelFuture channelFuture  = bootstrap.connect(host, port).sync();
                Channel channel = channelFuture.channel();
                // 发送数据
                channel.writeAndFlush(request);
                channel.closeFuture().sync();
                // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
                // AttributeKey是，线程隔离的，不会由线程安全问题。
                // 实际上不应通过阻塞，可通过回调函数
                AttributeKey<Response> key = AttributeKey.valueOf("Response");
                response = channel.attr(key).get();

            } catch (InterruptedException e) {
                log.warn(e.getMessage(), e);
                response = Response.fail("An error occurred when Netty handle request : " + e.getMessage());
            }

            return response;
        }
    };

}
