package com.andrew.wiki.service;

import com.andrew.wiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class WsService {

    @Autowired
    private WebSocketServer webSocketServer;

    //send message to all user
    @Async
    public void sendInfo(String docName, String userName, String logId){
        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(docName + " was Voted by " + userName);
    }
}
