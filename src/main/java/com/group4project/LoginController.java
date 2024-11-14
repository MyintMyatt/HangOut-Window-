package com.group4project;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.glassfish.tyrus.server.Server;

public class LoginController implements Initializable {

    @FXML
    private Pane loginPane;

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passField;

    @FXML
    private Button btn;

    @FXML
    private Label label;

    @FXML
    private ProgressIndicator indicator;

    private Connection con = null;

    private Thread thread_1;

    @FXML
    public void submit(ActionEvent event) {
        submitToLogin(event);
    }

    public void submitToLogin(Event event) {
        thread_1 = new Thread(() -> {
            Platform.runLater(() -> {
                indicator.setVisible(true);
                loginPane.setVisible(false);
            });
            con = AppData.getObj().getConnection();
            if (con == null) {
                con = Database.createConnection();
                AppData.getObj().setConnection(con);
            }

            String id = idField.getText();
            String password = passField.getText();
            String staff_type;

            try {
                PreparedStatement st = con.prepareStatement("select * from staff where staff_id = ? and password = ? and status != 'Inactive' ;");
                st.setString(1, id);
                st.setString(2, password);

                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    List<LoginStaff> loginStaffs = new ArrayList<>();
                    loginStaffs.add(new LoginStaff(rs.getString("staff_id"), rs.getString("staff_name")));
                    AppData.getObj().setLoginStaffList(loginStaffs);
                    staff_type = rs.getString("staff_type");
                    switch (staff_type.toLowerCase()) {
                        case "admin":
                            new Thread(() -> {
                                List<AllMenu> menuList = Database.getAllMenu(con);
                                List<Staff> staffList = Database.getAllStaff(con);
                                List<Category> categoryList = Database.categoryList(con);
                                AppData.getObj().setMenus(menuList);
                                AppData.getObj().setStaffList(staffList);
                                AppData.getObj().setCategoryList(categoryList);

                                Platform.runLater(() -> {
                                    indicator.setVisible(false);
                                    ChangePage.changePage(event, "adminPage.fxml");
                                });
                            }).start();
                            break;

                        case "waiter":
                            Platform.runLater(() -> {
                                indicator.setVisible(true);
                                loginPane.setVisible(false);
                            });
                            new Thread(() -> {
                                List<AllMenu> list = Database.getAllMenu(con);
                                List<Table> tableList = Database.getAllTables(con);
                                AppData.getObj().setMenus(list);
                                AppData.getObj().setTableList(tableList);
                                Platform.runLater(() -> {
                                    indicator.setVisible(false);
                                    ChangePage.changePage(event, "tablePage.fxml");
                                });
                            }).start();
                            break;
                        case "chef":
                            Platform.runLater(() -> {
                                indicator.setVisible(false);
                                ChangePage.changePage(event, "kitchenPage.fxml");
                            });
                            break;
                    }
                } else {
                    Platform.runLater(() -> {
                        indicator.setVisible(false);
                        loginPane.setVisible(true);
                        label.setText("Invalid ID or password");
                    });

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        thread_1.start();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                passField.requestFocus();
            }
        });

        passField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitToLogin(e);
            }
        });
        setupCloseRequestHandler(AppData.getObj().getPrimaryStage());

    }
    private void setupCloseRequestHandler(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {

            WebSocketAdmin webSocketAdmin = AppData.getObj().getWebSocketAdmin();
            Server server = AppData.getObj().getServer();
            if (server != null && webSocketAdmin != null) {
                webSocketAdmin.sendDeleteMessageToClient("server was shut down");
            }

            if (thread_1 != null && thread_1.isAlive()) {
                thread_1.interrupt();
            }
            Connection con = AppData.getObj().getConnection();
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            Platform.exit();
            System.exit(0);
        });
    }
}
