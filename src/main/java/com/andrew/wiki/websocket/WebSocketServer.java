package com.andrew.wiki.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * Every client side has a token
     */
    private String token = "";

    private static HashMap<String, Session> map = new HashMap<>();

    /**
     * connected successfully
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        map.put(token, session);
        this.token = token;
        LOG.info("[Websocket] New Connection：token：{}，session id：{}，Current Numbers of Connections：{}", token, session.getId(), map.size());
    }

    /**
     * Disconnection
     */
    @OnClose
    public void onClose(Session session) {
        map.remove(this.token);
        LOG.info("[Websocket]  Disconnected，token：{}，session id：{}！Current Numbers of Connections：{}", this.token, session.getId(), map.size());
    }

    /**
     * Receive Message
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("Received：{}，Content：{}", token, message);
    }

    /**
     *  Connection Error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error("Error during connection", error);
    }

    /**
     * Send Message to All
     */
    public void sendInfo(String message) {
        for (String token : map.keySet()) {
            Session session = map.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("[Websocket]  Send failed：{}，Content：{}", token, message);
            }
            LOG.info("[Websocket]  Send Message：{}，Content：{}", token, message);
        }
    }

}
