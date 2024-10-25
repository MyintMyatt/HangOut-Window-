package com.group4project;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javax.websocket.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ClientEndpoint
public class WaiterWebSocketClient {
    private static Session session;

    public static Session getSession() {
        return session;
    }

    public static void setSession() {
        session = null;
    }

    @OnOpen
    public void onOpen(Session s) {
        session = s;
        System.out.println("Waiter connected to server : " + session.getId());
    }

    /*for menu delete from admin*/
    @OnMessage
    public void onMessage(String menuId) {
        try {
            if (menuId.toLowerCase().equals("server was shut down")) {
                Platform.runLater(() -> {
                    WaiterPageController obj = new WaiterPageController();
                    obj.showAlertServerConnectionFailed();
//                    TablePageController tblObj = new TablePageController();
//                    tblObj.showAlertServerConnectionFailed();
                });
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            if (menuId instanceof String) {
                try {
                    int id = Integer.parseInt(menuId.trim());
                    List<AllMenu> list = AppData.getObj().getMenus();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getMenu_id() == id) {
                            list.remove(i);
                            break;
                        }
                    }
                    Platform.runLater(() -> {
                        updateUI();
                    });
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    @OnMessage
    public void onMessage(ByteBuffer message) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(message.array());
            ObjectInputStream objIn = new ObjectInputStream(in);
            Object object = objIn.readObject();
            List<AllMenu> allMenuList = AppData.getObj().getMenus();
            /*for new menu*/
            if (object instanceof AllMenu) {
                AllMenu newMenu = (AllMenu) object;
                List<AllMenu> list = AppData.getObj().getMenus();
                list.add(newMenu);
                Platform.runLater(() -> {
                    updateUI();
                });
            }
            /*for menu update*/
            try {
                if (object instanceof Map<?, ?>) {
                    Map<Integer, AllMenu> map = (Map<Integer, AllMenu>) object;
                    Set<Integer> keySet = map.keySet();
                    Iterator<Integer> iterator = keySet.iterator();
                    int id = iterator.next();
                    AllMenu menuObj = map.get(id);
                    for (int i = 0; i < allMenuList.size(); i++) {
                        if (id == allMenuList.get(i).getMenu_id()) {
                            allMenuList.get(i).setStocks(menuObj.getStocks());
                            allMenuList.get(i).setPrice(menuObj.getPrice());
                            allMenuList.get(i).setPhoto(menuObj.getPhoto());
                            break;
                        }
                    }
                    Platform.runLater(() -> {
                        updateUI();
                    });
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            /*for update stocks*/
            try {
                if (object instanceof Map<?, ?>) {
                    Map<Integer, Integer> stockMap = (Map<Integer, Integer>) object;
                    Set<Integer> keySet = stockMap.keySet();
                    Iterator<Integer> iterator = keySet.iterator();
                    while (iterator.hasNext()) {/*for multiple menu*/
                        int menuId = iterator.next();
                        for (int i = 0; i < allMenuList.size(); i++) {
                            if (menuId == allMenuList.get(i).getMenu_id()) {
                                int oldStock = allMenuList.get(i).getStocks();
                                allMenuList.get(i).setStocks(oldStock - stockMap.get(menuId));
                                break;
                            }
                        }
                    }
                }
                Platform.runLater(() ->{
                    updateUI();
                });
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Waiter disconnected from server : " + session.getId());
    }

    public static void disconnect() {
        try {
            if (session != null && session.isOpen()) {
                session.close();
                System.out.println("Waiter disconnect from server!!!!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUI() {
        Label categoryLabel = AppData.getObj().getWaiterCategoryLabel();
        String category = categoryLabel.getText();
        List<GridPane> gridPaneList = AppData.getObj().getGridPaneListWaiter();
        WaiterPageController obj = new WaiterPageController();
        switch (category.toLowerCase()) {
            case "main dishes":
                obj.setMenuPaneNewOne(gridPaneList.get(0), "1");
                break;
            case "drinks":
                obj.setMenuPaneNewOne(gridPaneList.get(1), "2");
                break;
            case "starters":
                obj.setMenuPaneNewOne(gridPaneList.get(2), "3");
                break;
            case "soup":
                obj.setMenuPaneNewOne(gridPaneList.get(3), "4");
                break;
            case "searching menu":
            case "":
                obj.searchingMenu(gridPaneList.get(4), "");
                break;
        }
    }

}
