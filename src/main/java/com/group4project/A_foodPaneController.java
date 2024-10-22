package com.group4project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class A_foodPaneController implements Initializable {
    @FXML
    private Label foodNamelb;

    @FXML
    private ImageView image;

    @FXML
    private Label pricelb;

    private Connection con;

    private Pane updatePane;

    private TextField search_tf, price_tf, current_stocks_tf, add_stocks_tf;

    private ImageView u_image;

    private List<AllMenu> allMenuList;

    private List<GridPane> gridPaneList;


    @FXML
    void updateBtnAction(ActionEvent event) {

        AppData.getObj().setMenuName(foodNamelb.getText());//for update stocks

        add_stocks_tf.clear();
        double price = Double.parseDouble(pricelb.getText().split("Ks")[0]);
        updatePane.setVisible(true);
        price_tf.setText(String.valueOf(price));
        if (allMenuList != null) {
            for (int i = 0; i < allMenuList.size(); i++) {
                if (allMenuList.get(i).getMenu_name().equals(foodNamelb.getText()) && allMenuList.get(i).getPrice() == price) {
                    current_stocks_tf.setText("Current Stocks : " + allMenuList.get(i).getStocks());
                    AppData.getObj().setUpdate_id(allMenuList.get(i).getMenu_id());
                    break;
                }
            }
        }
        u_image.setImage(image.getImage());
    }

    @FXML
    void deleteBtnAction(ActionEvent event) {
        double price = Double.parseDouble(pricelb.getText().split("Ks")[0]);
        if (allMenuList != null) {
            for (int i = 0; i < allMenuList.size(); i++) {
                if (allMenuList.get(i).getMenu_name().equals(foodNamelb.getText()) && allMenuList.get(i).getPrice() == price) {
                    Optional<ButtonType> buttonType = AlertClass.askConfirmAlert("Are you sure wanna delete??");
                    if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                        int id = allMenuList.get(i).getMenu_id();
                        allMenuList = Database.deleteMenu(con, id);
                        int selected = AppData.getObj().getSelect_pane();
                        AdminPageController.getObj().deleteMenu(allMenuList, gridPaneList, search_tf, selected);

                        /*send delete menu to waiter page*/
                        AppData.getObj().getWebSocketAdmin().sendDeleteMessageToClient(String.valueOf(id));
                    }
                }
            }
        }
    }

    public void setValue(AllMenu menu) {
        foodNamelb.setText(menu.getMenu_name());
        pricelb.setText(menu.getPrice() + "Ks");
        image.setImage(new Image(new ByteArrayInputStream(menu.getPhoto())));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = AppData.getObj().getConnection();
        allMenuList = AppData.getObj().getMenus();
        gridPaneList = AppData.getObj().getGridPaneListAdmin();
        search_tf = AppData.getObj().getSearch_tf();
        updatePane = AppData.getObj().getUpdatePane();
        price_tf = AppData.getObj().getPrice_tf();
        current_stocks_tf = AppData.getObj().getCurrent_stock_tf();
        add_stocks_tf = AppData.getObj().getAdd_stocks_tf();
        u_image = AppData.getObj().getU_image();

       current_stocks_tf.setEditable(false);/*?*/

    }
}
