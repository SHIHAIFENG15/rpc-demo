package com.demo.server;

import com.demo.codec.Decoder;
import com.demo.codec.Encoder;
import com.demo.codec.JSONDecoder;
import com.demo.codec.JSONEncoder;
import com.demo.transport.HTTPTransportServer;
import com.demo.transport.TransportServer;
import lombok.Data;


/**
 * @author: shf
 * @description:
 * @date: 2022/5/1 16:22
 */

@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HTTPTransportServer.class;

    private Class<? extends Encoder> encoderClass = JSONEncoder.class;

    private Class<? extends Decoder> decoderClass = JSONDecoder.class;

    /*
    默认端口
     */
    private int port = 3000;
}
