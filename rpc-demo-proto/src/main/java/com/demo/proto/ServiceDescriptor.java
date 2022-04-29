package com.demo.proto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author: shf
 * @description: 表示服务信息抽象
 * @date: 2022/4/29 17:14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDescriptor implements Serializable {

    /*
     * 类名
     * */
    private String clazz;

    /*
     * 方法名
     */
    private String method;

    /*
     * 返回类型
     */
    private String returnType;

    /*
    参数类型
     */
    private String[] parameterTypes;

    /*
     * @param clazz:
     * @param method:
     * @return ServiceDescriptor
     * @description 由class和method包装Service信息
     * @date 2022/4/27 20:53
     */
    public static ServiceDescriptor from(Class clazz, Method method) {
        ServiceDescriptor sdp = new ServiceDescriptor();

        sdp.setClazz(clazz.getName());
        sdp.setMethod(method.getName());
        sdp.setReturnType(method.getReturnType().getName());

        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] paramTypes = new String[parameterTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = parameterTypes[i].getName();
        }
        sdp.setParameterTypes(paramTypes);

        return sdp;
    }

//    // 由于注册服务用map使用该自建类型，需要重写该类equals和hashcode方法
//    @Override
//    public int hashCode() {
//        return toString().hashCode();
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ServiceDescriptor that = (ServiceDescriptor) o;
//
//        return this.toString().equals(that.toString());
//    }
//
//    @Override
//    public String toString() {
//        return "clazz=" + clazz
//                + ",method=" + method
//                + ",returnType=" + returnType
//                + ",parameterTypes=" + Arrays.toString(parameterTypes);
//    }
}

