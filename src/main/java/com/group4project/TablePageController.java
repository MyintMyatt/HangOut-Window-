package com.group4project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.websocket.Session;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TablePageController implements Initializable {

    @FXML
    private GridPane tableGP;

    @FXML
    private Pane confirmBookingPane;

    @FXML
    private Label customerName;

    @FXML
    private Label tblNumber;

    @FXML
    private TextField passwordTF;

    private List<Table> tableList;

    private Timer timer;

    private Connection con;


    private String serverIpAddress;

    @FXML
    void confirmBtnAction(ActionEvent event) {
        List<Table> tableList = AppData.getObj().getTableList();
        int num = Integer.parseInt(tblNumber.getText().trim());
        String name = customerName.getText();
        for (int i = 0; i < tableList.size(); i++) {
            if (tableList.get(i).getTableNumber() == num && tableList.get(i).getBooking_name().equals(name)) {
                String pw = passwordTF.getText();
                if (tableList.get(i).getBooking_pass().equals(pw)) {
                    if (WaiterWebSocketClient.getSession() != null) {
                        ChangePage.changePage(event, "waiterPage.fxml");
                        Label label = AppData.getObj().getS_Page_tblNumber();
                        label.setText("Table - " + num);
                        TableController obj = new TableController();
                        obj.changeTableStatus(tableList, num);
                        break;
                    } else {
                        AlertClass.errorAlert("Start Server First!!");
                    }
                } else {
                    AlertClass.errorAlert("Invalid Password");
                    break;
                }
            }
        }
    }


    @FXML
    void cancelBtnAction(ActionEvent event) {
        confirmBookingPane.setVisible(false);
        passwordTF.setText("");
        customerName.setText("");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = AppData.getObj().getConnection();

        if (WaiterWebSocketClient.getSession() == null) {
            con = AppData.getObj().getConnection();
            /*connect to server*/
            serverIpAddress = Database.getServerIpAddress(con);
            String uri = "ws://" + serverIpAddress + ":8080/ws/hangOutServer";
            WebSocketClient webSocketClient = new WebSocketClient(uri);
            AppData.getObj().setWebSocketClient(webSocketClient);
        }
        AppData.getObj().setConfirmBookingPaneElements(confirmBookingPane, customerName, tblNumber);
        tableList = AppData.getObj().getTableList();
        loadingTable();

        /*checking table status in db*/
        new Thread(() -> {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    List<Table> tables = Database.getAllTables(con);
                    if (!tableList.equals(tables)) {
                        AppData.getObj().setTableList(tables);
                        tableList = AppData.getObj().getTableList();
                        Platform.runLater(() -> {
                            loadingTable();
                        });
                    }
                }
            }, 0, 1000);// 1s
        }).start();
        setupCloseRequestHandler(AppData.getObj().getPrimaryStage());
    }

    private void setupCloseRequestHandler(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            if (timer != null) {
                timer.cancel();
            }
            Platform.exit();
            System.exit(0);
        });
    }

    private void loadingTable() {
        tableGP.getChildren().clear();
        List<Table> tableList = AppData.getObj().getTableList();
        int col = 0;
        int row = 1;
        for (int i = 0; i < tableList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("table.fxml"));
                AnchorPane pane = fxmlLoader.load();
                TableController controller = fxmlLoader.getController();
                controller.setValue(tableList.get(i));
                if (col == 6) {
                    col = 0;
                    ++row;
                }

                tableGP.add(pane, col++, row);
                GridPane.setMargin(pane, new Insets(15));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void logout(ActionEvent event) {
        Optional<ButtonType> result = AlertClass.askConfirmAlert("Are you sure ????");
        if (result.isPresent() && result.get() == ButtonType.OK) {

            WaiterWebSocketClient.disconnect();/*1*/
            WaiterWebSocketClient.setSession();/*2*/
            ChangePage.changePage(event, "loginPage.fxml");
        }
    }
    public void showAlertServerConnectionFailed() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        AlertClass.errorAlert("Server was shut down on " + formatter.format(LocalDateTime.now()));
    }
}
