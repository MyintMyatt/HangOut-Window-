package com.group4project;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class KitchenOrderPageController implements Initializable {
    @FXML
    private GridPane orderItemGP;

    @FXML
    private Pane upperPane;

    @FXML
    private AnchorPane orderPane;

    @FXML
    private Label orderNoLbl;

    @FXML
    private Label orderedTimeLbl;

    @FXML
    private Label tblNoLbl;

    @FXML
    private Label orderStatusLbl;

    private GridPane orderGP;

    private Map<String, List<KitchenOrderDetails>> kitchenOrderDetailsMap = AppData.getObj().getKitchenOrderDetailsMap();

    @FXML
    void orderFinishBtnAction(ActionEvent event) {
        if (checkAlMenuOrdersAreFinished() == null) {
            /*For Order GridPane's Row and Col*/
            int row = KitchenPageController.getObj().getRow1();
            int col = KitchenPageController.getObj().getCol1();
            if (col == 0 && row != 1) {
                KitchenPageController.getObj().setRow1(--row);
                KitchenPageController.getObj().setCol1(4);
            }else
                KitchenPageController.getObj().setCol1(--col);

            String orderId = orderNoLbl.getText();
            List<KitchenOrderDetails> list = kitchenOrderDetailsMap.get(orderId);


            if (list.size() == 1) { // there is only One KitchenOrderDetails Object in list
                kitchenOrderDetailsMap.remove(orderId);//for removing from kitchen page
            }else {
                String orderTime = orderedTimeLbl.getText().trim();
                for (int i = 0; i < list.size(); i++) {
                    KitchenOrderDetails obj = list.get(i);
                    if (orderTime.equals(obj.getOrderTime())) {
                        list.remove(list.get(i));//for deleting order pane and orderDetailsObject when finish btn is clicked
                        kitchenOrderDetailsMap.put(orderId, list);
                    }
                }
            }

            if (kitchenOrderDetailsMap != null) {
                Platform.runLater(() ->{
                    KitchenPageController.getObj().loadingOrderPane(kitchenOrderDetailsMap, orderGP);
                });
            }
        }else {
            AlertClass.errorAlert(checkAlMenuOrdersAreFinished()+ " is not done yet!!!");
        }
    }

    private String checkAlMenuOrdersAreFinished() {
        String tem = null;
        ObservableList<Node> observableList = orderItemGP.getChildren();
        for (Node node : observableList) {
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                CheckBox checkBox = (CheckBox) pane.getChildren().get(2);
                if (!checkBox.isSelected()) {
                    Label menuName = (Label) pane.getChildren().get(0);
                    tem = menuName.getText();
                }
            }
        }
        return tem;

    }

    @FXML
    void orderStartBtnAction(ActionEvent event) {
        orderStatusLbl.setStyle("-fx-text-fill : #00df82;");
        orderStatusLbl.setText("Preparing");
        /*change orderStatus to Preparing in map*/
        String orderId = orderNoLbl.getText();
        String orderTime = orderedTimeLbl.getText();
        List<KitchenOrderDetails> list = kitchenOrderDetailsMap.get(orderId);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getOrderTime().equals(orderTime)) {
                list.get(i).setOrderStatus("Preparing");
            }
        }
    }

    private int row = 1;
    private int col = 0;

    public void setNewValueToOrderPane(KitchenOrderDetails obj, String orderId) {
        List<KitchenOrderDetails> kitchenOrderDetailsList = kitchenOrderDetailsMap.get(orderId);
        if (kitchenOrderDetailsList == null) {//for avoiding null value exception
            kitchenOrderDetailsList = new ArrayList<>();
        }
        /*adding new kitchenOrderDetails Object to kitchenOrderDetailsList*/
        kitchenOrderDetailsList.add(obj);
        kitchenOrderDetailsMap.put(orderId, kitchenOrderDetailsList);

        setValueToOrderPane(obj, orderId);
    }

    public void setOldValueToOrderPane(KitchenOrderDetails obj, String orderId) {
        setValueToOrderPane(obj, orderId);
    }

    public void setValueToOrderPane(KitchenOrderDetails obj, String orderId) {
        String orderStatus = obj.getOrderStatus();
        int tblNo = obj.getTableNo();
        String orderTime = obj.getOrderTime();

        if (orderStatus.equals("Preparing")) {
            orderStatusLbl.setStyle("-fx-text-fill : #00df82;");
        }

        orderNoLbl.setText(orderId);
        tblNoLbl.setText("Table No : " + tblNo);
        orderedTimeLbl.setText(orderTime);
        orderStatusLbl.setText(orderStatus);

        List<OrderItems> orderItemsList = obj.getOrderItemsList();
        for (int i = 0; i < orderItemsList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("kitchenOrderDetailsPane.fxml"));
                Pane root = fxmlLoader.load();
                KitchenOrderDetailsPaneController controller = fxmlLoader.getController();
                controller.setValueOrderItemsDetails(orderItemsList.get(i));

                if (col == 1) {
                    col = 0;
                    ++row;
                }

                orderItemGP.add(root, col++, row);
                GridPane.setMargin(root, new Insets(2));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderGP = AppData.getObj().getOrderGP();
        AppData.getObj().setOrderStatusLbl(orderStatusLbl);
    }

}
