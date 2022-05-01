package com.demo.transport;

import com.demo.proto.Peer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/1 11:15
 */

@Slf4j
public class HTTPTransportClient implements TransportClient{
    private String url;

    @Override
    public void connect(Peer peer) {
        this.url = "http://" + peer.getHost() + ":" + peer.getPort();
    }

    @Override
    public InputStream write(InputStream data) {
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();

            // 需要输入输出
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 不需要缓存
            conn.setUseCaches(false);
            // POST
            conn.setRequestMethod("POST");

            conn.connect();

            // 写入数据
            IOUtils.copy(data, conn.getOutputStream());

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return conn.getInputStream();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return null;
    }

    @Override
    public void close() {

    }
}
