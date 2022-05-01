package com.demo.codec;

/**
 * @author: shf
 * @description: 反序列化
 * @date: 2022/4/30 1:08
 */

public interface Decoder {
    <T> T decode(byte[] bytes, Class<T> clazz);
}
