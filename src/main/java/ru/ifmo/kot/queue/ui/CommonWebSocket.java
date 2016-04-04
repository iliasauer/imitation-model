package ru.ifmo.kot.queue.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class CommonWebSocket extends WebSocketAdapter {

    private static final Logger LOGGER = LogManager.getLogger(CommonWebSocket.class);


    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
    }

    @Override
    public void onWebSocketError(Throwable ignored) {
        LOGGER.error("The internal system error");
    }
}
