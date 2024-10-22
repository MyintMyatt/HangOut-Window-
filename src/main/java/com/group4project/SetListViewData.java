package com.group4project;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class SetListViewData {

    private ListView<HBox> listView;
    private Label totalAmount;

    public void setListViewData(int orderId) {
        listView = AppData.getObj().getListView();
        listView.getItems().clear();
        totalAmount = AppData.getObj().getTotal_amount();

        try {
            Connection con = AppData.getObj().getConnection();
            CallableStatement call = con.prepareCall("{call getOrderDetails(?)};");
            call.setInt(1, orderId);
            System.out.println("order Id " + orderId);
            ResultSet rs = call.executeQuery();
            while (rs.next()) {
                Button plus = new Button("+");
                Button minus = new Button("-");
                Label name = new Label();
                Label qtyLabel = new Label();
                Label priceLabel = new Label();

                name.setText(rs.getString("menu_name"));
                qtyLabel.setText(String.valueOf(rs.getInt("quantity")));
                double totalPrice = rs.getDouble("total_price");
                priceLabel.setText(String.valueOf(totalPrice));

                HBox hBox = new HBox(name, plus, qtyLabel, minus, priceLabel);
                listView.getItems().add(hBox);
                totalPrice += Double.parseDouble(totalAmount.getText().split("Ks")[0]);
                totalAmount.setText(totalPrice + "Ks");

                plus.setMinSize(20, 20);
                minus.setMinSize(20, 20);

                /*Style of listView components*/
                name.setPrefWidth(150);
                name.setFont(Font.font(15));
                qtyLabel.setPrefWidth(30);
                qtyLabel.setAlignment(Pos.CENTER);
                priceLabel.setPrefWidth(150);
                priceLabel.setFont(Font.font(15));
                priceLabel.setAlignment(Pos.CENTER);

                AtomicReference<Double> unitPrice = new AtomicReference<>((double) 0);

                plus.setOnAction(event1 -> {
                    calculateUnitPrice(unitPrice, priceLabel, qtyLabel);

                    int count = Integer.parseInt(qtyLabel.getText());
                    qtyLabel.setText(String.valueOf(++count));//don't write count++ that does not work
                    priceLabel.setText((count * unitPrice.get()) + "Ks");

                    String totalArr[] = totalAmount.getText().split("Ks");
                    double total = Double.parseDouble(totalArr[0]);
                    totalAmount.setText((total + unitPrice.get()) + "Ks");

                });
                minus.setOnAction(e -> {
                    calculateUnitPrice(unitPrice, priceLabel, qtyLabel);

                    int count = Integer.parseInt(qtyLabel.getText());
                    if (count > 1) {
                        qtyLabel.setText(String.valueOf(--count));
                        priceLabel.setText((count * unitPrice.get()) + "Ks");

                        String totalArr[] = totalAmount.getText().split("Ks");
                        double total = Double.parseDouble(totalArr[0]);
                        totalAmount.setText((total - unitPrice.get()) + "Ks");
                    }

                });

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void calculateUnitPrice(AtomicReference<Double> unitPrice, Label priceLabel, Label qtyLabel) {
        if (unitPrice.get() == 0) {
            int qty = Integer.parseInt(qtyLabel.getText());
            String array[] = priceLabel.getText().split("Ks");
            double totalPrice = Double.parseDouble(array[0]);
            unitPrice.set(totalPrice / qty);
        }
    }
}
