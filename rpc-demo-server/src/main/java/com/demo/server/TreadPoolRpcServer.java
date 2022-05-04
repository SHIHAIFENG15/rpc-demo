package com.demo.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/3 15:06
 */

@Slf4j
public class TreadPoolRpcServer implements RpcServer{
    private final ThreadPoolExecutor threadPool;
    private ServiceManager serviceManager;

    public TreadPoolRpcServer(ServiceManager manager) {
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                            1000,
                                60,
                                            TimeUnit.SECONDS,
                                            new ArrayBlockingQueue<>(100));
        this.serviceManager = manager;
    }

    public TreadPoolRpcServer(ServiceManager manager,
                              int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.serviceManager = manager;
    }

    @Override
    public <T> void register(Class<T> interfaceClass, T bean) {
        serviceManager.register(interfaceClass, bean);
    }

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            log.info("Server listening...");
            // BIO的方式监听Socket
            while (true){
                Socket socket = serverSocket.accept();
                // 提交线程池处理
                threadPool.execute(new WorkThread(socket, serviceManager));
            }
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void stop() {

    }
}
