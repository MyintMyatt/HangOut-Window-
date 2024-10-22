package com.group4project;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.nio.ByteBuffer;

public class WebSocketClient {

    private Session session;

    public WebSocketClient(String serverURI) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            session = container.connectToServer(WaiterWebSocketClient.class, new URI(serverURI));
            Platform.runLater(() -> {
                AlertClass.confirmAlert("Successfully connected to server!");
            });
        } catch (Exception e) {
            Platform.runLater(() -> {
                AlertClass.errorAlert(e.getMessage()+"\n Start Server!!!");
            });
        }
    }

    public void sendOrderToKitchen(ByteBuffer order) {
        if (session != null && session.isOpen()) {
            System.out.println("Send order from waiter.....");
            session.getAsyncRemote().sendBinary(order);
        }
    }

    public void sendUpdateStocksToAdmin(ByteBuffer stock) {
        if (session != null && session.isOpen()) {
            System.out.println("send update stock to admin....");
            session.getAsyncRemote().sendBinary(stock);
        }
    }
}
