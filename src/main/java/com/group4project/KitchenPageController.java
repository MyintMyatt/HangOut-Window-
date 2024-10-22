package com.group4project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class KitchenPageController implements Initializable {
    @FXML
    private GridPane orderGP;

    private static KitchenPageController obj = new KitchenPageController();

    public static KitchenPageController getObj() {
        return obj;
    }

    @FXML
    void logOut(ActionEvent event) {
        Optional<ButtonType> result = AlertClass.askConfirmAlert("Are you sure ????");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ChangePage.changePage(event, "loginPage.fxml");
        }
    }

    private int row1 = 1;
    private int col1 = 0;

    public int getCol1() {
        return col1;
    }

    public void setCol1(int col1) {
        this.col1 = col1;
    }

    public int getRow1() {
        return row1;
    }

    public void setRow1(int row1) {
        this.row1 = row1;
    }

    public void addNewOrderPane(Map<String, KitchenOrderDetails> listMap, GridPane orderGP) {
        Set<String> orderIdSet = listMap.keySet();
        Iterator<String> iterator = orderIdSet.iterator();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("kitchenOrderPage.fxml"));
            AnchorPane root = fxmlLoader.load();
            KitchenOrderPageController controller = fxmlLoader.getController();

            /*for orderId*/
            String orderId = iterator.next();

            KitchenOrderDetails obj = listMap.get(orderId); // get KitchenOrderDetails object
            controller.setNewValueToOrderPane(obj, orderId);

            if (col1 == 5) {
                col1 = 0;
                ++row1;
            }

            orderGP.add(root, col1++, row1);
            GridPane.setMargin(root, new Insets(23));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadingOrderPane(Map<String, List<KitchenOrderDetails>> listMap, GridPane orderGP) {
        orderGP.getChildren().clear();/*to avoid overlapping pane in GridPane*/
        int row = 1;
        int col = 0;
        Set<String> orderIdSet = listMap.keySet();
        Iterator<String> iterator = orderIdSet.iterator();

        for (int i = 0; i < listMap.size(); i++) {
            /*for orderId*/
            String orderId = iterator.next();
            List<KitchenOrderDetails> kitchenOrderDetailsList = listMap.get(orderId);
            for (int j = 0; j < kitchenOrderDetailsList.size(); j++) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("kitchenOrderPage.fxml"));
                    AnchorPane root = fxmlLoader.load();
                    KitchenOrderPageController controller = fxmlLoader.getController();
                    KitchenOrderDetails obj = kitchenOrderDetailsList.get(j);
                    controller.setOldValueToOrderPane(obj, orderId);

                    if (col == 5) {
                        col = 0;
                        ++row;
                    }

                    orderGP.add(root, col++, row);
                    GridPane.setMargin(root, new Insets(23));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppData.getObj().setOrderGP(orderGP);
        /*connected to server*/
        try {
            String serverIpAddress = Database.getServerIpAddress(AppData.getObj().getConnection());
            String serverURL = "ws://" + serverIpAddress.trim() + ":8080/ws/hangOutServer";
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(KitchenWebSocketClient.class, new URI(serverURL));
            Platform.runLater(() ->{
                AlertClass.confirmAlert("Successfully connected to server!");
            });
        } catch (Exception e) {
            Platform.runLater(() -> {
                AlertClass.errorAlert(e.getMessage()+"\n Start Server!!!");
            });

        }
    }
}
