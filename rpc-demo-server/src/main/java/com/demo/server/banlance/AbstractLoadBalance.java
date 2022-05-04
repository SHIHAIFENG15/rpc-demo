package com.demo.server.banlance;

import java.util.List;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/4 17:39
 */

public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public String balance(List<String> addressList, String rpcService) {
        if (addressList == null || addressList.size() == 0)
            return null;
        if (addressList.size() == 1) return addressList.get(0);
        return doSelect(addressList, rpcService);
    }

    protected abstract String doSelect(List<String> addressList, String rpcService);
}
