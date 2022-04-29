package com.demo.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author: shf
 * @description: 服务实体，通过实体代理方法
 * @date: 2022/4/29 19:27
 */

@Data
@AllArgsConstructor
public class ServiceInstance {
    private Object target;

    private Method method;
}
