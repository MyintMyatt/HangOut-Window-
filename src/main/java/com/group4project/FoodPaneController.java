package com.group4project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class FoodPaneController implements Initializable {
    @FXML
    private Label foodName;

    @FXML
    private ImageView image;

    @FXML
    private Label price;

    @FXML
    private Spinner<Integer> spinner;

    private ListView<HBox> listView;

    private Label totalAmount;

    private int spinnerValue;

    Button plus;
    Button minus;
    Label name, qtyLabel, priceLabel;


    @FXML
    void addBtnAction(ActionEvent event) {
        if (spinnerValue > 0) {
            plus = new Button("+");
            minus = new Button("-");

            plus.setMinSize(20, 20);
            minus.setMinSize(20, 20);

            AtomicReference<Double> unitPrice = new AtomicReference<>((double) 0);

            plus.setOnAction(event1 -> {
                calculateUnitPrice(unitPrice);
                List<AllMenu> allMenuList = AppData.getObj().getMenus();

                int count = Integer.parseInt(qtyLabel.getText());
                int maxQty = -1;
                String menuName = name.getText();
                for (int i = 0; i < allMenuList.size(); i++) {
                    if (allMenuList.get(i).getMenu_name().equals(menuName.trim())) {
                        maxQty = allMenuList.get(i).getStocks();
                        break;
                    }
                }
                if (count < maxQty) {
                    qtyLabel.setText(String.valueOf(++count));//don't write count++ that does not work
                    priceLabel.setText((count * unitPrice.get()) + "Ks");

                    String totalArr[] = totalAmount.getText().split("Ks");
                    double total = Double.parseDouble(totalArr[0]);
                    totalAmount.setText((total + unitPrice.get()) + "Ks");
                }

            });
            minus.setOnAction(e -> {
                calculateUnitPrice(unitPrice);

                int count = Integer.parseInt(qtyLabel.getText());
                if (count > 1) {
                    qtyLabel.setText(String.valueOf(--count));
                    priceLabel.setText((count * unitPrice.get()) + "Ks");

                    String totalArr[] = totalAmount.getText().split("Ks");
                    double total = Double.parseDouble(totalArr[0]);
                    totalAmount.setText((total - unitPrice.get()) + "Ks");
                }

            });

            name = new Label(foodName.getText());
            qtyLabel = new Label(String.valueOf(spinnerValue));

            String arr[] = price.getText().split("K");
            double unitPrice0 = Double.parseDouble(arr[0]);
            priceLabel = new Label((spinnerValue * unitPrice0) + "Ks");

            /*Style of listView components*/
            name.setPrefWidth(150);
            name.setFont(Font.font(15));
            qtyLabel.setPrefWidth(30);
            qtyLabel.setAlignment(Pos.CENTER);
            priceLabel.setPrefWidth(150);
            priceLabel.setFont(Font.font(15));
            priceLabel.setAlignment(Pos.CENTER);


            HBox hBox = new HBox(name, plus, qtyLabel, minus, priceLabel);

            listView.getItems().add(hBox);

            /*Setting Total Amount*/
            String totalArr[] = totalAmount.getText().split("Ks");
            double total = Double.parseDouble(totalArr[0]);
            totalAmount.setText((total + (unitPrice0 * spinnerValue)) + "Ks");
        }
    }

    private void calculateUnitPrice(AtomicReference<Double> unitPrice) {
        if (unitPrice.get() == 0) {
            int qty = Integer.parseInt(qtyLabel.getText());
            String array[] = priceLabel.getText().split("Ks");
            double totalPrice = Double.parseDouble(array[0]);
            unitPrice.set(totalPrice / qty);
        }
    }

    @FXML
    void spinnerAction(MouseEvent event) {
        spinnerValue = spinner.getValue().intValue();
    }

    public void setValue(AllMenu allMenu) {
        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, allMenu.getStocks(), 0);
        foodName.setText(allMenu.getMenu_name());
        price.setText(allMenu.getPrice() + "Ks");
        image.setImage(new Image(new ByteArrayInputStream(allMenu.getPhoto())));
        spinner.setValueFactory(svf);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView = AppData.getObj().getListView();
        totalAmount = AppData.getObj().getTotal_amount();
    }

}
