package com.demo.server.banlance;

import com.demo.server.banlance.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * @author: shf
 * @description: 随机策略负载均衡
 * @date: 2022/5/4 16:52
 */
@Slf4j
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> addressList, String rpcService) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        log.info("Random LoadBalance choose Server: " + choose);
        return addressList.get(choose);
    }
}
