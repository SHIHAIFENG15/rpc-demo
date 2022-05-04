package com.demo.server.banlance;

import java.util.List;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/4 16:57
 */

public class RoundLoadBalance extends AbstractLoadBalance{
    private int choose = -1;

    @Override
    protected String doSelect(List<String> addressList, String rpcService) {
        choose++;
        choose = choose%addressList.size();
        return addressList.get(choose);
    }
}
