package com.demo.proto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: shf
 * @description: 网络节点抽象
 * @date: 2022/5/1 11:13
 */

@Data
@AllArgsConstructor
public class Peer {

    private String host;

    private int port;
}
