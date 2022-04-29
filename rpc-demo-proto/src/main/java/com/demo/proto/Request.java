package com.demo.proto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: shf
 * @description: 表示RPC的一个请求
 * @date: 2022/4/29 17:15
 */

@Data
public class Request implements Serializable {

    private ServiceDescriptor service;

    private Object[] parameters;
}

