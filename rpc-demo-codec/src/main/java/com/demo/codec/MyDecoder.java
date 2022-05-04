package com.demo.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: shf
 * @description: 为Netty设计的多种实现方式的解码器
 * @date: 2022/5/3 20:08
 */

public class MyDecoder extends ByteToMessageDecoder{
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        short messageType = byteBuf.readShort();

        // 读取类型
        if (messageType != MessageType.REQUEST.code &&
            messageType != MessageType.RESPONSE.code) {
            throw new IllegalStateException("Wrong Data Type.");
        }

        // 读取序列化方式
        short serializerType = byteBuf.readShort();
        Serializer serializer = Serializer.getSerializerByCode(serializerType);
        if (serializer == null) {
            throw new IllegalStateException("Serializer type not exit : " + serializerType);
        }

        // 读取长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        // 解码数据，返回
        Object o = serializer.deserialize(bytes, messageType);
        list.add(o);
    }
}
