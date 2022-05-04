package com.demo.server.register;

import com.demo.proto.ServiceDescriptor;

import java.net.InetSocketAddress;

public interface ServiceRegister {
    void register(ServiceDescriptor serviceDescriptor, InetSocketAddress serverAddress);

    InetSocketAddress serviceDiscovery(ServiceDescriptor serviceDescriptor);
}
