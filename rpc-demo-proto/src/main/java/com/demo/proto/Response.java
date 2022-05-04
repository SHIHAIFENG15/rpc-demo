package com.demo.proto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: shf
 * @description: 表示RPC的返回
 * @date: 2022/4/29 17:15
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response implements Serializable {

    private int code;

    private String message;

    private Object data;

    private String clazz;

    public static Response success(Object data, String clazz) {
        return Response.builder()
                .code(200)
                .data(data)
                .clazz(clazz)
                .build();
    }

    public static Response fail(String message) {
        return Response.builder()
                .code(500)
                .message(message)
                .build();
    }
}