package com.group4project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.glassfish.tyrus.server.Server;

public class WaiterPageController implements Initializable {

    private WebSocketClient webSocketClient;

    @FXML
    private ScrollPane coolDrinkPane, dishPane;

    @FXML
    private ListView<HBox> listView;

    @FXML
    private Label staffNameLbl;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label tblLabel;


    /*For loading All menu*/
    private List<AllMenu> menus = null;

    private List<LoginStaff> loginStaffList;

    private Connection con;

    @FXML
    void tableBtnAction(ActionEvent event) {
        listView.getItems().clear();
        totalAmountLabel.setText("0.0Ks");
        ChangePage.changePage(event, "tablePage.fxml");
    }

    @FXML
    void removeItems(ActionEvent event) {
        int index = listView.getSelectionModel().getSelectedIndex();
        HBox hBox = listView.getSelectionModel().getSelectedItem();
        if (hBox != null) {
            Label priceLabel = (Label) hBox.getChildren().get(4);
            String[] arr = priceLabel.getText().split("Ks");
            String[] totalarr = totalAmountLabel.getText().split("Ks");
            totalAmountLabel.setText((Double.parseDouble(totalarr[0]) - Double.parseDouble(arr[0]) + "Ks"));
        }
        if (index > -1) {
            listView.getItems().remove(index);
        }

    }

    @FXML
    void cancelOrder(ActionEvent event) {
        totalAmountLabel.setText("0.0Ks");
        listView.getItems().clear();
    }

    /*Search Bar*/
    @FXML
    private ScrollPane searchPane;
    @FXML
    private GridPane searchGP;
    @FXML
    private TextField searchTF;

    public void searchingMenu(GridPane gp, String keyword) {
        gp.getChildren().clear();
        int col = 0;
        int row = 1;
        menus = AppData.getObj().getMenus();
        if (menus != null) {
            for (int i = 0; i < menus.size(); i++) {
                if (menus.get(i).getMenu_name().toLowerCase().contains(keyword)) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("WaiterFoodPane.fxml"));
                        Pane pane = fxmlLoader.load();
                        WaiterFoodPaneController controller = fxmlLoader.getController();
                        controller.setValue(menus.get(i));

                        if (col == 4) {
                            col = 0;
                            ++row;
                        }
                        gp.add(pane, col++, row);
                        GridPane.setMargin(pane, new Insets(13));
                    } catch (IOException e) {
                        AlertClass.errorAlert(e.getMessage());
                    }
                }
            }
        }
    }


    /*For Dynamic Dish Pane*/
    @FXML
    private GridPane dishGP;

    @FXML
    void dishesBtnAction(ActionEvent event) {
        dishPane.setVisible(true);
        searchPane.setVisible(false);
        coolDrinkPane.setVisible(false);
        starterPane.setVisible(false);
        soupPane.setVisible(false);

        categoryLabel.setText("Main Dishes");
        dishGP.getChildren().clear();

        setMenuPaneNewOne(dishGP, "1");
    }

    /*For Dynamic Cool Drink Pane*/
    @FXML
    private GridPane coolDrinkGP;

    @FXML
    void coolDrinksAction(ActionEvent event) {
        coolDrinkPane.setVisible(true);
        searchPane.setVisible(false);
        dishPane.setVisible(false);
        soupPane.setVisible(false);
        starterPane.setVisible(false);

        categoryLabel.setText("Drinks");
        coolDrinkGP.getChildren().clear();
        setMenuPaneNewOne(coolDrinkGP, "2");

    }

    /*For Starter Pane*/
    @FXML
    private ScrollPane starterPane;

    @FXML
    private GridPane starterGP;

    @FXML
    void starterBtnAction(ActionEvent event) {
        searchPane.setVisible(false);
        dishPane.setVisible(false);
        coolDrinkPane.setVisible(false);
        soupPane.setVisible(false);
        starterPane.setVisible(true);

        categoryLabel.setText("Starters");
        starterGP.getChildren().clear();

        setMenuPaneNewOne(starterGP, "3");
    }

    /*For Soup Pane*/
    @FXML
    private ScrollPane soupPane;

    @FXML
    private GridPane soupGP;

    @FXML
    void soupBtnAction(ActionEvent event) {
        searchPane.setVisible(false);
        dishPane.setVisible(false);
        dishPane.setVisible(false);
        starterPane.setVisible(false);
        soupPane.setVisible(true);

        categoryLabel.setText("Soup");
        soupGP.getChildren().clear();

        setMenuPaneNewOne(soupGP, "4");
    }

    @FXML
    void logOut(ActionEvent event) {
        Optional<ButtonType> result = AlertClass.askConfirmAlert("Are you sure ????");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ChangePage.changePage(event, "loginPage.fxml");

            WaiterWebSocketClient.disconnect();/*1*/
            WaiterWebSocketClient.setSession();/*2*/
        }
    }

    public void setMenuPaneNewOne(GridPane gp, String category_id) {
        int col = 0;
        int row = 1;

        gp.getChildren().clear();
        menus = AppData.getObj().getMenus();
        if (menus != null) {
            for (int i = 0; i < menus.size(); i++) {
                if (category_id.equals(menus.get(i).getCategory_id())) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("WaiterFoodPane.fxml"));
                        Pane pane = fxmlLoader.load();

                        WaiterFoodPaneController controller = fxmlLoader.getController();
                        controller.setValue(menus.get(i));

                        if (col == 4) {
                            col = 0;
                            ++row;
                        }
                        gp.add(pane, col++, row);
                        GridPane.setMargin(pane, new Insets(13));

                    } catch (IOException e) {
                        AlertClass.errorAlert(e.getMessage());
                    }
                }
            }
        }
    }

//    public void refreshUI() {
//        switch (categoryLabel.getText().toLowerCase()) {
//            case "main dishes":
//                setMenuPaneNewOne(dishGP, "1");
//                break;
//            case "drinks":
//                setMenuPaneNewOne(coolDrinkGP, "2");
//                break;
//            case "starters":
//                setMenuPaneNewOne(starterGP, "3");
//                break;
//            case "soup":
//                setMenuPaneNewOne(soupGP, "4");
//                break;
//            case "searching menu":
//            case "":
//                searchingMenu(searchGP, "");
//                break;
//        }
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = AppData.getObj().getConnection();

        stageOnCloseAction(AppData.getObj().getPrimaryStage());

        webSocketClient = AppData.getObj().getWebSocketClient();

        menus = AppData.getObj().getMenus();
        loginStaffList = AppData.getObj().getLoginStaffList();
        staffNameLbl.setText(loginStaffList.get(0).getName());


        AppData.getObj().setListView(listView);
        AppData.getObj().setTotal_amount(totalAmountLabel);
        AppData.getObj().setS_Page_tblNumber(tblLabel);

        List<GridPane> gridPaneList = new ArrayList<>();
        gridPaneList.add(dishGP);
        gridPaneList.add(coolDrinkGP);
        gridPaneList.add(starterGP);
        gridPaneList.add(soupGP);
        gridPaneList.add(searchGP);


        AppData.getObj().setGridPaneListWaiter(gridPaneList);
        AppData.getObj().setWaiterCategoryLabel(categoryLabel);

        searchTF.textProperty().addListener((observableValue, oldvalue, newvalue) -> {
            categoryLabel.setText("Searching Menu");
            searchPane.setVisible(true);
            dishPane.setVisible(false);
            coolDrinkPane.setVisible(false);
            starterPane.setVisible(false);
            soupPane.setVisible(false);

            searchingMenu(searchGP, newvalue.toLowerCase());
        });
    }
    private void stageOnCloseAction(Stage stage) {
        stage.setOnCloseRequest(e -> {
            WebSocketAdmin webSocketAdmin = AppData.getObj().getWebSocketAdmin();
            Server server = AppData.getObj().getServer();

            if (server != null && webSocketAdmin != null) /*if admin started server and he logined in staff page, to send server down message  */
                webSocketAdmin.sendDeleteMessageToClient("server was shut down");
            else
                System.exit(0);/*for only staff*/

        });
    }

    private int orderId;
    private String staffId;
    private int tableNo;
    private int row = 0;//for new order items
    private Map<Integer, Integer> orderMap;
    private Map<Integer, Integer> rowMap;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd / HH:mm:ss");

    @FXML
    void orderBtnAction(ActionEvent event) {
        Optional<ButtonType> result = AlertClass.askConfirmAlert("Are you sure wanna order???");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            staffId = loginStaffList.get(0).getId();
            String tblNo = tblLabel.getText().trim().substring(tblLabel.getText().length() - 2);
            tableNo = Integer.parseInt(tblNo.trim());

            orderMap = AppData.getObj().getOrderMap();/*to store orderNo with table number*/
            /*orderMap <Table Number, Order No>  key value is Table Number*/
            rowMap = AppData.getObj().getRowMap();/*for listView row (for new order item)*/
            /*rowMap<Order No, Row Number> key value is Order No*/

            if (listView.getItems().size() > 0) {
                orderId = (orderMap.get(tableNo) != null) ? orderMap.get(tableNo) : -1;
                if (orderId == -1) {
                    orderId = Database.addNewOrder(con, staffId, tableNo);
                    orderMap.put(tableNo, orderId);
                    AppData.getObj().setOrderMap(orderMap);
                }
                /*for listview row*/
                row = (rowMap.get(orderId) != null) ? rowMap.get(orderId) : 0;
                List<OrderDetails> orderDetailsList = new ArrayList<>();/*storing data in db*/
                int listViewSize = listView.getItems().size();

                /*storing data for kitchen order*/
                List<OrderItems> orderItemsList = new ArrayList<>();
                Map<String, KitchenOrderDetails> kitchenOrderDetailsMap = new HashMap<>();
                Map<Integer, Integer> stockMap = new HashMap<>();/*to send stocks to admin*/

                for (int i = row; i < listViewSize; i++) {
                    HBox hBox = listView.getItems().get(i);
                    Label menuNameLbl = (Label) hBox.getChildren().get(0);
                    Label qtyLbl = (Label) hBox.getChildren().get(2);
                    Label priceLbl = (Label) hBox.getChildren().get(4);
                    String menuName = menuNameLbl.getText();
                    int menuId = -1;

                    int qty = Integer.parseInt(qtyLbl.getText().trim());
                    String total = priceLbl.getText().substring(0, priceLbl.getText().length() - 2);
                    double totalPrice = Double.parseDouble(total);
                    double unitPrice = totalPrice / qty;

                    menus = AppData.getObj().getMenus();
                    for (int j = 0; j < menus.size(); j++) {
                        if (menus.get(j).getMenu_name().equals(menuName)) {
                            menuId = menus.get(j).getMenu_id();/*searching menu id by menu name*/

                            /*update menu stocks for admin*/
                            Set<Integer> keySet = stockMap.keySet();
                            Iterator<Integer> iterator = keySet.iterator();
                            while (iterator.hasNext()) {/*if there is duplicate key in stockMap, to avoid overriding stock qty*/
                                if (menuId == iterator.next()) {
                                    int stock = stockMap.get(menuId);
                                    qty += stock;
                                }
                            }
                            stockMap.put(menuId, qty);
                            break;
                        }
                    }
                    orderDetailsList.add(new OrderDetails(menuId, qty, unitPrice, totalPrice));

                    orderItemsList.add(new OrderItems(menuName, qty));
                    row++;
                }
                rowMap.put(orderId, row);


                /*to send update stocks to admin*/
                byte[] stockBytes = changeObjToByte(stockMap);
                webSocketClient.sendUpdateStocksToAdmin(ByteBuffer.wrap(stockBytes));

                if (orderItemsList.size() > 0) {/*to avoid null value order to kitchen*/
                    LocalDateTime now = LocalDateTime.now();
                    kitchenOrderDetailsMap.put("orderId : " + orderId, new KitchenOrderDetails(orderItemsList, "waiting", tableNo, formatter.format(now)));

                    /*to send order to kitchen*/
                    byte[] orderBytes = changeObjToByte(kitchenOrderDetailsMap);
                    webSocketClient.sendOrderToKitchen(ByteBuffer.wrap(orderBytes));
                }


                /*for storing orders in database*/
                for (int i = 0; i < orderDetailsList.size(); i++) {
                    int menuId = orderDetailsList.get(i).getMenuId();
                    int qty = orderDetailsList.get(i).getQuantity();
                    double unitPrice = orderDetailsList.get(i).getUnitPice();
                    double totalPice = orderDetailsList.get(i).getTotalPrice();
                    /*to add orderDetails in database*/
                    Database.addOrderDetails(con, orderId, menuId, qty, unitPrice, totalPice);
                    /*to update menu stocks in database*/
                    Database.updateStock(con, menuId, qty);
                }
                double totalAmount = Double.parseDouble(totalAmountLabel.getText().substring(0, totalAmountLabel.getText().length() - 2));
                Database.updateTotalAmount(con, orderId, totalAmount);
            } else
                AlertClass.errorAlert("Null Value !!");
        }
    }

    @FXML
    void receiptBtnAction(ActionEvent event) throws JRException {
        try {
            String tblNo = tblLabel.getText().trim().substring(tblLabel.getText().length() - 2);
            tableNo = Integer.parseInt(tblNo.trim());
            orderMap = AppData.getObj().getOrderMap();
            rowMap = AppData.getObj().getRowMap();
            orderId = orderMap.get(tableNo);
            InputStream path = getClass().getResourceAsStream("hangoutReceipt.jrxml");
            // JasperReport jasperReport = JasperCompileManager.compileReport(path);

           // JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("reports/hangoutReceipt.jrxml"));
            InputStream inputStream = getClass().getResourceAsStream("hangoutReceipt.jrxml");
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);


            Map<String, Object> data = new HashMap<>();
            data.put("orderNo", orderId);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, data, con);
            JasperViewer.viewReport(jasperPrint, false);

            listView.getItems().clear();

            /*Change row and orderId*/
            rowMap.remove(orderId);
            orderMap.put(tableNo, -1);

            Database.changeOrderPaymentStatus(con, orderId);
            Database.changeTableStatus(con, tableNo, 0);

        } catch (Exception e) {
            AlertClass.errorAlert(e.getMessage());
            e.printStackTrace();
//            AlertClass.errorAlert("Please Order First !!!");
        }
    }

    public void showAlertServerConnectionFailed() {
        AlertClass.errorAlert("Server was shut down on " + formatter.format(LocalDateTime.now()));
    }

    /*change map object to buffer byte*/
    private byte[] changeObjToByte(Map<?, ?> map) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(map);
            objOut.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
