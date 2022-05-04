package com.demo.codec;

import com.demo.proto.Request;
import com.demo.proto.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import jdk.nashorn.internal.ir.RuntimeNode;
import lombok.AllArgsConstructor;


/**
 * @author: shf
 * @description: 为Netty设计的多种实现方式的编码器
 * @date: 2022/5/3 19:50
 */

@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder {
    private Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 协议格式： 消息类型（2Byte）	序列化方式 2Byte	消息长度 4Byte
        // 写入类型
        if(o instanceof Request){
            byteBuf.writeShort(MessageType.REQUEST.getCode());
        } else if (o instanceof Response) {
            byteBuf.writeShort(MessageType.RESPONSE.getCode());
        }

        // 写入序列化方式
        byteBuf.writeShort(serializer.getType());

        // 得到序列化数组
        byte[] serialize = serializer.serialize(o);
        // 写入长度
        byteBuf.writeInt(serialize.length);
        // 写入序列化字节数组
        byteBuf.writeBytes(serialize);
    }
}
