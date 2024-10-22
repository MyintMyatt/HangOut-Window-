package com.group4project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class KitchenOrderDetailsPaneController implements Initializable {
    @FXML
    private Label menuLbl;

    @FXML
    private Label qtyLbl;

    @FXML
    private CheckBox checkBox;

    private Label orderStatusLbl;

    public void setValueOrderItemsDetails(OrderItems orderItems) {
        menuLbl.setText(orderItems.getMenuName());
        qtyLbl.setText(String.valueOf(orderItems.getQty()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderStatusLbl = AppData.getObj().getOrderStatusLbl();
        checkBox.setOnAction(e -> {

            String orderStatus = orderStatusLbl.getText();
            if (orderStatus.toLowerCase().equals("waiting")) {
                AlertClass.confirmAlert("Press Start Button First.!!!");
                checkBox.setSelected(false);
            } else {
                checkBox.setSelected(true);
            }
        });
    }
}
