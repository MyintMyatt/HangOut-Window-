package com.group4project;

import javafx.application.Platform;
import org.glassfish.tyrus.server.Server;
import javax.websocket.DeploymentException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;

public class MainServer {
    public static void startServer() {
        Server server = null;
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String serverIpAddress = localhost.getHostAddress().trim();
            AppData.getObj().setServerIpAddress(serverIpAddress);
            Database.setServerIpAddress(AppData.getObj().getConnection(), serverIpAddress);
            server = new Server(serverIpAddress, 8080, "/ws", new HashMap<>(), ServerEndPoint.class);
            server.start();
            System.out.println("WebSocket Server Started........");
            Platform.runLater(() -> {
                AlertClass.confirmAlert("The Server was started with ip address " + serverIpAddress);
            });
            Thread.currentThread().join();
        } catch (InterruptedException | DeploymentException | UnknownHostException e) {
            Platform.runLater(()->{
                AlertClass.errorAlert("Server was already started!!");
            });
        }finally {
            if (server != null) {
                server.stop();
            }

        }
    }
}
