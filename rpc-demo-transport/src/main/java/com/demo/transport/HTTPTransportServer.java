package com.demo.transport;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author: shf
 * @description:
 * @date: 2022/5/1 16:06
 */

@Slf4j
public class HTTPTransportServer implements TransportServer{
    private RequestHandler handler;

    // jetty使用servlet
    private Server server;

    @Override
    public void init(int port, RequestHandler handler) {
        this.handler = handler;
        this.server = new Server(port);

        // Servlet的初始化使用：自定义一个继承于HttpServlet的Servlet，以及它对应的空间
        // 相当于工程目录，如果是myweb，则http://localhost:port/myweb/
        ServletContextHandler context = new ServletContextHandler();
        server.setHandler(context);
        ServletHolder holder = new ServletHolder(new RequestServlet());
        context.addServlet(holder, "/*");
    }

    @Override
    public void start() {
        try {
            server.start();
            // 等待start结束
            server.join();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private class RequestServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            log.info("Client connect...");

            InputStream inputStream = req.getInputStream();
            OutputStream outputStream = resp.getOutputStream();

            if (handler != null) {
                handler.onRequest(inputStream, outputStream);
            }

            outputStream.flush();
        }
    }
}
