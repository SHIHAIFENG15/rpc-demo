package com.demo.codec;

/**
 * @author: shf
 * @description: 序列化
 * @date: 2022/4/30 1:07
 */

public interface Encoder {
    byte[] encode(Object obj);
}