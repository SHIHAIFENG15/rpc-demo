package com.demo.client;

import com.demo.common.utils.ReflectionUtils;
import com.demo.proto.Peer;
import com.demo.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: shf
 * @description: 随机挑选策略
 * @date: 2022/5/1 12:17
 */

@Slf4j
public class RandomTransportSelector implements TransportSelector{
    private List<TransportClient> clients;

    public RandomTransportSelector() {
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz) {
        count = Math.max(1, count);

        for (Peer peer : peers) {
            for (int i = 0; i < count; i++) {
                // 反射添加TransportClient接口
                TransportClient client = ReflectionUtils.newInstance(clazz);
                client.connect(peer);
                clients.add(client);
            }

            log.info("Connect server: " + peer);
        }
    }

    @Override
    public synchronized TransportClient select() {
        int index = new Random().nextInt(clients.size());
        return clients.remove(index);
    }

    @Override
    public synchronized void release(TransportClient client) {
        clients.add(client);
    }

    @Override
    public synchronized void close() {
        for (TransportClient client : clients) {
            client.close();
        }

        clients.clear();
    }
}
