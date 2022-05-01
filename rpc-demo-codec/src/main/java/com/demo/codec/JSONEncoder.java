package com.demo.codec;

import com.alibaba.fastjson.JSON;

/**
 * @author: shf
 * @description:
 * @date: 2022/4/30 1:11
 */

public class JSONEncoder implements Encoder{
    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }
}
