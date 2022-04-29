package com.demo.common.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: shf
 * @description: 反射工具类
 * @date: 2022/4/29 19:31
 */

public class ReflectionUtils {
    /*
     * @param clazz: 待创建对象的类
     * @param <T> 对象类型
     * @return T: 创建好的对象,利用泛型确定返回类型
     * @description 根据class创建对象
     * @date 2022/4/27 11:33
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * @param clazz:
     * @return Method 当前类声明的共有方法
     * @description 获取某个class的共有方法
     * @date 2022/4/27 11:52
     */
    public static Method[] getPublicMethods(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> pubMethods = new ArrayList<>();

        for (Method m : methods) {
            // 修饰符为public则获取
            if (Modifier.isPublic(m.getModifiers())) {
                pubMethods.add(m);
            }
        }

        // 传入数组长度小于实际值自动扩容
        return pubMethods.toArray(new Method[0]);
    }

    /*
     * @param obj: 被调用方法的使用对象
     * @param method: 被调用的方法
     * @param args: 方法的参数
     * @return Object
     * @description 调用指定对象的指定方法
     * @date 2022/4/27 11:57
     */
    public static Object invoke(Object obj, Method method,
                                Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
