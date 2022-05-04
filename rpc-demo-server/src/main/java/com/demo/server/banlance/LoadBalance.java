package com.demo.server.banlance;

import java.util.List;

/**
 * @author: shf
 * @description: 负载均衡接口
 * @date: 2022/5/4 16:51
 */

public interface LoadBalance {
    String balance(List<String> addressList, String rpcService);
}
