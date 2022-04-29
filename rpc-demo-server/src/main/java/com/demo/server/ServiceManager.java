package com.demo.server;

import com.demo.common.utils.ReflectionUtils;
import com.demo.proto.Request;
import com.demo.proto.ServiceDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: shf
 * @description: 管理rpc暴露的服务: 1.注册服务 2.查找服务
 * @date: 2022/4/29 19:28
 */

@Slf4j
public class ServiceManager {
    /*
    利用Map注册
     */
    private Map<ServiceDescriptor, ServiceInstance> services;

    public ServiceManager() {
        this.services = new ConcurrentHashMap<>();
    }

    public <T> void register(Class<T> interfaceClass, T bean) {
        // 提取类所有公共方法
        Method[] methods = ReflectionUtils.getPublicMethods(interfaceClass);

        for (Method method : methods) {
            ServiceInstance instance = new ServiceInstance(bean, method);
            ServiceDescriptor sdp = ServiceDescriptor.from(interfaceClass, method);

            services.put(sdp, instance);
            log.info("Register service : "
                    + sdp.getClazz()
                    + sdp.getMethod());
        }
    }

    public ServiceInstance lookup(Request request) {
        return services.get(request.getService());
    }
}
