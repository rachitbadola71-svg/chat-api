package com.RACTEC.CHATAPI.websocket; // must match the folder, or IntelliJ shows red

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        Long roomId = (Long) session.getAttributes().get("roomId");
        rooms.computeIfAbsent(roomId,id ->ConcurrentHashMap.newKeySet()).add(session);
    }

    public void sendToRoom(Long roomId, String json){
        Set<WebSocketSession> sessions = rooms.get(roomId);
        if(sessions == null){return ; }
        for (WebSocketSession s : sessions) {
            if(s.isOpen()){
                try{
                    synchronized (s){
                        s.sendMessage(new TextMessage(json));}
                } catch (IOException e) {
                   sessions.remove(s) ;
                }
            }
        }
    }
}
