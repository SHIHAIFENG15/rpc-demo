package com.demo.server;

import com.demo.common.utils.ReflectionUtils;
import com.demo.proto.Request;
import com.demo.proto.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.AllArgsConstructor;

/**
 * @author: shf
 * @description: Netty Server具体的Handler
 * @date: 2022/5/3 16:29
 */

@AllArgsConstructor
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<Request> {
    private ServiceManager serviceManager;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request request) throws Exception {
        Response response = getResp(request);
        channelHandlerContext.writeAndFlush(response);
        channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private Response getResp(Request request) {
        ServiceInstance instance = serviceManager.lookup(request);
        Object invoke = ReflectionUtils.invoke(
                instance.getTarget(),
                instance.getMethod(),
                request.getParameters());
        return Response.success(invoke, request.getService().getReturnType());
    }
}
