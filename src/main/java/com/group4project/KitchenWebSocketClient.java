package com.group4project;

import javafx.application.Platform;

import javax.websocket.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.Map;

@ClientEndpoint
public class KitchenWebSocketClient {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Kitchen connected to server : " + session.getId());
    }

    @OnMessage
    public void onMessage(ByteBuffer byteBuffer) throws IOException, ClassNotFoundException {
        System.out.println("Kitchen received message");
        //deserialize binary data to original map
        Platform.runLater(() -> {
            try {
                ByteArrayInputStream bin = new ByteArrayInputStream(byteBuffer.array());
                ObjectInputStream objIn = new ObjectInputStream(bin);
                Object object = objIn.readObject();
                if (object instanceof Map<?, ?>) { // filter map or list //map is used for sending orders to kitchen and list is used for sending menu to waiter
                    Map<String, KitchenOrderDetails> kitchenOrderDetailsMap = (Map<String, KitchenOrderDetails>) object;
                    KitchenPageController.getObj().addNewOrderPane(kitchenOrderDetailsMap, AppData.getObj().getOrderGP());
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Kitchen disconnected from server :  " + session.getId());
    }

}
