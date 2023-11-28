package com.jiawa.wiki.service;

import com.jiawa.wiki.domain.Doc;
import com.jiawa.wiki.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {
    @Resource
    WebSocketServer webSocketServer;

    /**
     * 异步推送消息，为了和点赞+1功能解耦，使得两者之间的关联减小
     * @param message
     */
    @Async
    public void sendInfo(String message){
        // 推送消息
        webSocketServer.sendInfo(message);
    }
}
