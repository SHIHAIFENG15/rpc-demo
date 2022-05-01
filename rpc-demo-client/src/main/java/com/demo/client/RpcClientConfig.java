package com.demo.client;

import com.demo.codec.Decoder;
import com.demo.codec.Encoder;
import com.demo.codec.JSONDecoder;
import com.demo.codec.JSONEncoder;
import com.demo.proto.Peer;
import com.demo.transport.HTTPTransportClient;
import com.demo.transport.TransportClient;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author: shf
 * @description: 通过该配置类定义实际实现类，完成解耦
 * @date: 2022/5/1 15:25
 */

@Data
public class RpcClientConfig {
    private Class<? extends TransportClient> transportClass
            = HTTPTransportClient.class;
    private Class<? extends Encoder> encodeClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private Class<? extends TransportSelector> selectorClass
            = RandomTransportSelector.class;
    private int connectCount = 1;
    private List<Peer> servers = Arrays.asList(
            new Peer("127.0.0.1", 3000)
    );
}
