package com.demo.codec;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/3 19:56
 */

@Getter
@AllArgsConstructor
public enum MessageType {
    REQUEST(0),

    RESPONSE(1);

    int code;
}
