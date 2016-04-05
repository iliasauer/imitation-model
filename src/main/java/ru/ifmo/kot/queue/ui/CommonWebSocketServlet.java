package ru.ifmo.kot.queue.ui;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class CommonWebSocketServlet extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(CommonWebSocket.class);
    }
}
