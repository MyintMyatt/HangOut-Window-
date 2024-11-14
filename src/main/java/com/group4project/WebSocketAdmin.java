package com.group4project;

import javafx.application.Platform;
import javafx.scene.control.Button;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.nio.ByteBuffer;

public class WebSocketAdmin {
    private Session session;

    public WebSocketAdmin(String serverURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(AdminWebSocketClient.class, new URI(serverURI));
            AlertClass.confirmAlert("Successfully connected to server!");
            Button connectToServerBtn = AppData.getObj().getConnectToServerBtn();
            connectToServerBtn.setText("Connected");
            connectToServerBtn.setStyle("-fx-text-fill: red");
        } catch (Exception e) {
            Platform.runLater(() -> {
                AlertClass.errorAlert("Start server first!!!");
            });
        }
    }

    public void sendMessageToClient(ByteBuffer menu) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendBinary(menu);
        }
    }


    public void sendDeleteMessageToClient(String message) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }
    }
}
