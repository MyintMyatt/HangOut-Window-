package com.group4project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class TableController implements Initializable {

    @FXML
    private Label table_lb;
    @FXML
    private Label status_lb;

    private Pane confirmBookingPane;

    private Label customerName;

    private Label tblNumber;

    private Label label; //// for waiter page's table number label

    private SetListViewData setListViewData;

    private Connection con;

    private WebSocketClient webSocketClient = null;

    private String serverIpAddress = null;


    @FXML
    void tableAction(MouseEvent event) {

        Map<Integer, Integer> map = AppData.getObj().getOrderMap();

        List<Table> tableList = AppData.getObj().getTableList();
        String tbl_no = table_lb.getText();
        String status = status_lb.getText().toLowerCase();
        int num = Integer.parseInt(tbl_no.substring(tbl_no.length() - 2).trim());

        switch (status) {
            case "available":
                webSocketClient = AppData.getObj().getWebSocketClient();
                if (webSocketClient != null) {
                    ChangePage.changePage(event, "homePage.fxml");
                    label = AppData.getObj().getS_Page_tblNumber();
                    label.setText(tbl_no);
                    changeTableStatus(tableList, num);
                }else
                    AlertClass.errorAlert("Start Server First !!!");
                break;
            case "reserved":
                confirmBookingPane.setVisible(true);
                for (int i = 0; i < tableList.size(); i++) {
                    if (tableList.get(i).getTableNumber() == num) {
                        customerName.setText(tableList.get(i).getBooking_name());
                        tblNumber.setText(String.valueOf(num));
                    }
                }
                break;
            case "unavailable":
                webSocketClient = AppData.getObj().getWebSocketClient();
                if (webSocketClient != null) {
                    ChangePage.changePage(event, "homePage.fxml");
                    label = AppData.getObj().getS_Page_tblNumber();
                    label.setText(tbl_no);
                    if (map.get(num) != null && map.get(num) != 0) {
                        setListViewData.setListViewData(map.get(num));
                    }
                }else
                    AlertClass.errorAlert("Start Server First !!!");
                break;

        }
    }

    public void changeTableStatus(List<Table> tableList, int tblNo) {
        Connection con = AppData.getObj().getConnection();
        for (int i = 0; i < tableList.size(); i++) {
            if (tableList.get(i).getTableNumber() == tblNo) {
                List<Table> list = Database.changeTableStatus(con, tblNo, 1);
                AppData.getObj().setTableList(list);
                break;
            }
        }
    }


    public void setValue(Table table) {
        table_lb.setText("Table - " + table.getTableNumber());
        int status = table.getTableStatus();
        switch (status) {
            case 0:
                status_lb.setText("Available");
                status_lb.setStyle("-fx-text-fill : #00df82");
                break;
            case 1:
                status_lb.setText("Unavailable");
                status_lb.setStyle("-fx-text-fill : red");
                break;
            case 2:
                status_lb.setText("Reserved");
                status_lb.setStyle("-fx-text-fill : blue");
                break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmBookingPane = AppData.getObj().getConfirmBookingPane();
        customerName = AppData.getObj().getCustomerName();
        tblNumber = AppData.getObj().getTblNumberLabel();

        setListViewData = new SetListViewData();
    }

}
