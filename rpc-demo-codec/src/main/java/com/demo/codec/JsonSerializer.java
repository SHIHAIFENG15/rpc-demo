package com.demo.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.proto.Request;
import com.demo.proto.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/3 20:25
 */

@Slf4j
public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return JSONObject.toJSONBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        // 传输的消息分为request与response
        switch (messageType){
            case 0:
                Request request = JSON.parseObject(bytes, Request.class);
                // 参数类型也要转化，否则都是JSONObject会出问题
                Object[] objects = request.getParameters();
                for (int i = 0; i < objects.length; i++) {
                    String classType= request.getService().getParameterTypes()[i];
                    // 把json字串转化成对应的对象， fastjson可以读出基本数据类型，不用转化
                    try {
                        Class<?> clazz = Class.forName(classType);
                        if (!clazz.isAssignableFrom(objects[i].getClass())) {
                            objects[i] = JSONObject.toJavaObject((JSONObject)objects[i], clazz);
                        }
                    } catch (ClassNotFoundException ignored) {

                    }
                }

                obj = request;
                break;
            case 1:
                Response response = JSON.parseObject(bytes, Response.class);
                String returnType = response.getClazz();
                try {
                    Class<?> clazz = Class.forName(returnType);
                    if(! clazz.isAssignableFrom(response.getData().getClass())){
                        response.setData(JSONObject.toJavaObject((JSONObject) response.getData(), clazz));
                    }
                } catch (ClassNotFoundException ignored) {

                }
                obj = response;
                break;
            default:
                log.warn("Deserialize error: can not supply this return type.");
                throw new IllegalStateException("Deserialize error: can not supply this return type.");
        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;
    }
}
