package com.demo.codec;

import com.alibaba.fastjson.JSON;

/**
 * @author: shf
 * @description:
 * @date: 2022/4/30 1:09
 */

public class JSONDecoder implements Decoder{
    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
