package com.group4project;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/hangOutServer")
public class ServerEndPoint {

    private static Set<Session> clientSet = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New Connection : " +  session.getId());
        clientSet.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed: " + session.getId());
        clientSet.remove(session);
    }

    @OnMessage
    public void onMessage(ByteBuffer message, Session session) {
        synchronized (clientSet) {
            for (Session client : clientSet) {
                if (client.isOpen()) {
                    client.getAsyncRemote().sendBinary(message);
                }
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        synchronized (clientSet) {
            for (Session client : clientSet) {
                if (client.isOpen()) {
                    client.getAsyncRemote().sendText(message);
                }
            }
        }
    }

}
