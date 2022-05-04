package com.demo.server.register;

import com.demo.proto.ServiceDescriptor;
import com.demo.server.banlance.ConsistentHashLoadBalance;
import com.demo.server.banlance.LoadBalance;
import com.demo.server.banlance.RandomLoadBalance;
import com.demo.server.register.ServiceRegister;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author: shf
 * @description: zookeeper注册中心
 * @date: 2022/5/4 16:00
 */

@Slf4j
public class ZkServiceRegister implements ServiceRegister {
    // curator 提供的zookeeper客户端
    private CuratorFramework client;
    // zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
    // 负载均衡
    private LoadBalance balance = new ConsistentHashLoadBalance();

    // 这里负责zookeeper客户端的初始化，并与zookeeper服务端建立连接
    public ZkServiceRegister(){
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        // zookeeper的地址固定，不管是服务提供者还是，消费者都要与之建立连接
        // sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系，
        // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别为tickTime 的2倍和20倍
        // 使用心跳监听状态
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .retryPolicy(policy)
                .namespace(ROOT_PATH)
                .build();

        this.client.start();
        log.info("Zookeeper register started successfully.");
    }

    @Override
    public void register(ServiceDescriptor serviceDescriptor, InetSocketAddress serverAddress) {
        try {
            // serviceName创建成永久节点，服务提供者下线时，不删服务名，只删地址
            if(client.checkExists().forPath("/" + serviceDescriptor.toString()) == null){
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath("/" + serviceDescriptor.toString());
            }
            // 路径地址，一个/代表一个节点
            String path = "/" + serviceDescriptor.toString() +"/"+ getServiceAddress(serverAddress);
            // 临时节点，服务器下线就删除节点
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
            log.info("Service registered in: " + serverAddress.toString());
        } catch (Exception e) {
            log.warn("Service already exits: " + serviceDescriptor, e);
        }
    }

    @Override
    public InetSocketAddress serviceDiscovery(ServiceDescriptor serviceDescriptor) {
        try {
            List<String> strings = client.getChildren().forPath("/" + serviceDescriptor.toString());
//            // 这里默认用的第一个，后面加负载均衡
//            String string = strings.get(0);
            return parseAddress(balance.balance(strings, serviceDescriptor.toString()));
        } catch (Exception e) {
            log.error("Can not find service: " + serviceDescriptor.toString() + e.getMessage(), e);
        }

        return null;
    }

    // 地址 -> XXX.XXX.XXX.XXX:port 字符串
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() +
                ":" +
                serverAddress.getPort();
    }
    // 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
