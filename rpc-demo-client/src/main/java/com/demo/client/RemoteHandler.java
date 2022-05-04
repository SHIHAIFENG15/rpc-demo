package com.demo.client;

import com.demo.proto.Request;
import com.demo.proto.Response;

public interface RemoteHandler {
    Response getResp(Request request);
}
