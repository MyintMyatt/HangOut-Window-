package com.group4project;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.glassfish.tyrus.server.Server;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AdminPageController implements Initializable {

    private WebSocketAdmin webSocketAdmin;

    private AdminFoodPaneController controller;

    private Connection con;

    @FXML
    private Button serverBtn;

    @FXML
    private Button connectToServerBtn;

    @FXML
    private ScrollPane staffManagementPane;

    @FXML
    private Pane staffManagementUpperPane;

    @FXML
    private GridPane staffManagementGP;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label staffNameLbl;

    @FXML
    private GridPane coolDrinkGP;

    @FXML
    private ScrollPane coolDrinkPane;

    @FXML
    private GridPane dishGP;

    @FXML
    private ScrollPane dishPane;

    @FXML
    private GridPane searchGP;

    @FXML
    private ScrollPane searchPane;

    @FXML
    private GridPane soupGP;

    @FXML
    private ScrollPane soupPane;

    @FXML
    private GridPane starterGP;

    @FXML
    private ScrollPane starterPane;

    private static AdminPageController obj = new AdminPageController();

    public static AdminPageController getObj() {
        return obj;
    }

    private List<AllMenu> menuList;

    private int selected_pane = 0;

    @FXML
    void staffManagementBtnAction(ActionEvent event) {
        staffManagementPane.setVisible(true);
        staffManagementUpperPane.setVisible(true);
        dishPane.setVisible(false);
        coolDrinkPane.setVisible(false);
        soupPane.setVisible(false);
        starterPane.setVisible(false);
        searchPane.setVisible(false);

        categoryLabel.setText("Staff Management");
        loadingStaffPane(staffManagementGP);
    }

    @FXML
    void dishesBtnAction(ActionEvent event) {
        selected_pane = 1;
        AppData.getObj().setSelect_pane(1);
        dishPane.setVisible(true);
        staffManagementPane.setVisible(false);
        staffManagementUpperPane.setVisible(false);
        coolDrinkPane.setVisible(false);
        soupPane.setVisible(false);
        starterPane.setVisible(false);
        searchPane.setVisible(false);

        categoryLabel.setText("Main Dishes");
        setMenuPane(dishGP, "1");
    }

    @FXML
    void coolDrinksAction(ActionEvent event) {
        selected_pane = 2;
        AppData.getObj().setSelect_pane(2);
        coolDrinkPane.setVisible(true);
        staffManagementPane.setVisible(false);
        staffManagementUpperPane.setVisible(false);
        dishPane.setVisible(false);
        soupPane.setVisible(false);
        starterPane.setVisible(false);
        searchPane.setVisible(false);

        categoryLabel.setText("Drinks");
        setMenuPane(coolDrinkGP, "2");
    }

    @FXML
    void starterBtnAction(ActionEvent event) {
        selected_pane = 3;
        AppData.getObj().setSelect_pane(3);
        starterPane.setVisible(true);
        staffManagementPane.setVisible(false);
        staffManagementUpperPane.setVisible(false);
        dishPane.setVisible(false);
        coolDrinkPane.setVisible(false);
        soupPane.setVisible(false);
        searchPane.setVisible(false);

        categoryLabel.setText("Starters");
        setMenuPane(starterGP, "3");
    }

    @FXML
    void soupBtnAction(ActionEvent event) {
        selected_pane = 4;
        AppData.getObj().setSelect_pane(4);
        soupPane.setVisible(true);
        staffManagementPane.setVisible(false);
        staffManagementUpperPane.setVisible(false);
        dishPane.setVisible(false);
        coolDrinkPane.setVisible(false);
        starterPane.setVisible(false);
        searchPane.setVisible(false);

        categoryLabel.setText("Soups");
        setMenuPane(soupGP, "4");
    }

    @FXML
    void logout(ActionEvent event) {
        Optional<ButtonType> result = AlertClass.askConfirmAlert("Are you sure ????");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ChangePage.changePage(event, "loginPage.fxml");
            AdminWebSocketClient.disconnect();
            AdminWebSocketClient.setSession();
        }
    }

    /*staff details*/
    @FXML
    private HBox staffDetailsPane;

    @FXML
    private Label staffDetailsIdLbl;

    @FXML
    private Label staffDetailsNameLbl;

    @FXML
    private Label staffDetailsTypeLbl;

    @FXML
    private Label staffDetailsPWLbl;

    @FXML
    private Label staffDetailsGenderLbl;

    @FXML
    private Label staffDetailsDOBLbl;

    @FXML
    private Label staffDetailsNRCLbl;

    @FXML
    private Label staffDetailsPhLbl;

    @FXML
    private ImageView staffDetailsProfile;

    @FXML
    private Label staffDetailsEmailLbl;

    @FXML
    private Label startDateForStaffDetails;

    @FXML
    private Label quitDateForStaffDetails;

    @FXML
    private Label staffStatusForStaffDetials;


    @FXML
    private Label staffDetailsAddressLbl;

    @FXML
    void staffDetailsBackBtnAction(ActionEvent event) {
        staffDetailsIdLbl.setText("");
        staffDetailsNameLbl.setText("");
        staffDetailsTypeLbl.setText("");
        staffDetailsPWLbl.setText("");
        staffDetailsGenderLbl.setText("");
        staffDetailsDOBLbl.setText("");
        staffDetailsNRCLbl.setText("");
        staffDetailsPhLbl.setText("");
        staffDetailsEmailLbl.setText("");
        staffDetailsAddressLbl.setText("");
        startDateForStaffDetails.setText("");
        staffStatusForStaffDetials.setText("");
        staffDetailsProfile.setImage(null);
        staffDetailsPane.setVisible(false);
    }

    public void loadingStaffPane(GridPane staffManagementGP) {
        staffManagementGP.getChildren().clear();
        List<Staff> staffList = AppData.getObj().getStaffList();
        Collections.sort(staffList, new StaffComparator());
        Collections.sort(staffList, new CompareStaffStatus());
        int row = 1;
        int col = 3;
        if (staffList != null) {
            for (int i = 0; i < staffList.size(); i++) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("staffPane.fxml"));
                    Pane pane = fxmlLoader.load();
                    StaffController controller = fxmlLoader.getController();
                    controller.setValue(staffList.get(i));

                    if (col == 4) {
                        col = 3;
                        ++row;
                    }
                    staffManagementGP.add(pane, col++, row);
                    GridPane.setMargin(pane, new Insets(5));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public void searchingStaff(String keyWord) {
        staffManagementGP.getChildren().clear();
        List<Staff> staffList = AppData.getObj().getStaffList();
        Collections.sort(staffList, new StaffComparator());
        Collections.sort(staffList, new CompareStaffStatus());

        int row = 1;
        int col = 3;
        if (staffList != null) {
            for (int i = 0; i < staffList.size(); i++) {
                if (staffList.get(i).getId().toLowerCase().contains(keyWord) || staffList.get(i).getName().toLowerCase().contains(keyWord) || staffList.get(i).getStaffType().toLowerCase().contains(keyWord)) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("staffPane.fxml"));
                        Pane pane = fxmlLoader.load();
                        StaffController controller = fxmlLoader.getController();
                        controller.setValue(staffList.get(i));

                        if (col == 4) {
                            col = 3;
                            ++row;
                        }
                        staffManagementGP.add(pane, col++, row);
                        GridPane.setMargin(pane, new Insets(5));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /*for add new staff*/
    @FXML
    private HBox addNewStaffPane;

    @FXML
    private TextField staffIdA;

    @FXML
    private TextField staffNameA;

    @FXML
    private ComboBox<String> staffTypeAComBox;

    @FXML
    private TextField pwA;

    @FXML
    private TextField nrcA;

    @FXML
    private TextField phA;

    @FXML
    private TextField mailA;

    @FXML
    private TextArea addressA;

    @FXML
    private DatePicker dobA;

    @FXML
    private ImageView newStaffProfile;

    @FXML
    private DatePicker startDateForNewStaff;

    private String s_id, s_image, s_name, s_pw, s_mail, s_ph, s_address, s_nrc, s_gender, s_type, s_dob, s_startDate;

    @FXML
    void cancelAddStaffAction(ActionEvent event) {
        staffIdA.setText("");
        staffNameA.setText("");
        staffTypeAComBox.setValue("");
        pwA.setText("");
        nrcA.setText("");
        phA.setText("");
        mailA.setText("");
        addressA.setText("");
        dobA.setValue(null);
        newStaffProfile.setImage(null);
        addNewStaffPane.setVisible(false);
        startDateForNewStaff.setValue(null);
    }

    @FXML
    void addNewStaffBtnAction(ActionEvent event) {
        addNewStaffPane.setVisible(true);
    }

    @FXML
    void registerNewStaffBtnAction(ActionEvent event) {
        if (webSocketAdmin != null) {
            if (checkingStaffData()) {
                s_id = staffIdA.getText();
                s_name = staffNameA.getText();
                if (s_image == null) {
                    AlertClass.errorAlert("Select staff Photo");
                } else {
                    Optional<ButtonType> option = AlertClass.askConfirmAlert("Are you sure?");
                    if (option.isPresent() && option.get() == ButtonType.OK) {
                        Staff staff = Database.addNewStaff(con, s_id, s_name, s_type, s_pw, s_gender, s_mail, s_ph, s_dob, s_address, s_image, s_nrc, s_startDate);
                        byte[] bytes = convertObjectToByteBuffer(staff);
                        webSocketAdmin.sendMessageToClient(ByteBuffer.wrap(bytes));
                        addNewStaffPane.setVisible(false);
                        s_image = null;
                        clearStaffTextFieldData();
                    }
                }
            }
        } else
            AlertClass.errorAlert("Connect to server");
    }

    private byte[] convertObjectToByteBuffer(Object object) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ObjectOutputStream objOut = new ObjectOutputStream(out);) {
            objOut.writeObject(object);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void importNewStaffImage(javafx.scene.input.MouseEvent event) {
        File imagePath = fileChooser.showOpenDialog(new Stage());
        newStaffProfile.setImage(new Image("file:/" + imagePath));
        s_image = imagePath.getPath();
    }

    @FXML
    void staffTypeAComBoxAction(ActionEvent event) {
        s_type = staffTypeAComBox.getValue();
    }

    @FXML
    void maleRadioBtnAction(ActionEvent event) {
        s_gender = "male";
    }

    @FXML
    void femaleRadioBtnAction(ActionEvent e) {
        s_gender = "female";
    }

    private boolean checkingStaffData() {
        if (staffIdA.getText().isEmpty()) {
            AlertClass.errorAlert("Enter Staff ID");
            return false;
        } else if (staffNameA.getText().isEmpty()) {
            AlertClass.errorAlert("Enter Staff Name");
            return false;
        } else if (s_type == null || s_type.isEmpty()) {
            AlertClass.errorAlert("Select Staff Type");
            return false;
        } else if (pwA.getText().isEmpty()) {
            AlertClass.errorAlert("Enter password");
            return false;
        } else if (s_gender == null) {
            AlertClass.errorAlert("Select Gender");
            return false;
        } else if (nrcA.getText().isEmpty()) {
            AlertClass.errorAlert("Enter NRC");
            return false;
        } else if (dobA.getValue() == null || dobA.getValue().isAfter(LocalDate.now())) {
            AlertClass.errorAlert("Enter date of birth");
            return false;
        } else if (phA.getText().isEmpty()) {
            AlertClass.errorAlert("Enter Phone Number");
            return false;
        } else if (mailA.getText().isEmpty()) {
            AlertClass.errorAlert("Enter E-mail address");
            return false;
        } else if (startDateForNewStaff.getValue() == null || startDateForNewStaff.getValue().isAfter(LocalDate.now())) {
            AlertClass.errorAlert("Enter Start Date");
            return false;
        } else if (addressA.getText().isEmpty()) {
            AlertClass.errorAlert("Enter Address");
            return false;
        } else {
            s_id = staffIdA.getText();
            s_name = staffNameA.getText();
            s_pw = pwA.getText();
            s_nrc = nrcA.getText();
            s_ph = phA.getText();
            s_mail = mailA.getText();
            s_address = addressA.getText();
            s_dob = String.valueOf(dobA.getValue());
            s_startDate = String.valueOf(startDateForNewStaff.getValue());
            return true;
        }
    }

    private void clearStaffTextFieldData() {
        staffIdA.setText("");
        staffNameA.setText("");
        staffTypeAComBox.setValue("");
        pwA.setText("");
        nrcA.setText("");
        dobA.setValue(LocalDate.now());
        phA.setText("");
        mailA.setText("");
        addressA.setText("");
        startDateForNewStaff.setValue(null);

        if (newStaffProfile.getImage() != null) {
            newStaffProfile.setImage(null);
        }
    }


    /*for staff Update*/
    @FXML
    private HBox staffUpdatePane;

    @FXML
    private TextField staffIdU;

    @FXML
    private TextField staffNameU;

    @FXML
    private ComboBox<String> staffTypeUComBox;

    @FXML
    private TextField pwU;

    @FXML
    private TextField nrcU;

    @FXML
    private DatePicker dobU;

    @FXML
    private TextField phU;

    @FXML
    private TextField mailU;

    @FXML
    private TextArea addressU;

    @FXML
    private ImageView staffImageU;

    @FXML
    private ComboBox<String> statusComBoxForStaffUpdate;

    @FXML
    private DatePicker startDateForStaffUpdate;

    @FXML
    private DatePicker quitDateForStaffUpdate;
    private String staffType, updateDob, updatePhoto, updateStatus, updateStartDate, updateQuitDate;

    @FXML
    void cancelStaffUpdateBtnAction(ActionEvent event) {
        staffIdU.setText("");
        staffNameU.setText("");
        staffTypeUComBox.setValue("");
        pwU.setText("");
        nrcU.setText("");
        dobU.setValue(null);
        phU.setText("");
        mailU.setText("");
        addressU.setText("");
        staffImageU.setImage(null);
        statusComBoxForStaffUpdate.setValue(null);
        startDateForStaffUpdate.setValue(null);
        quitDateForStaffUpdate.setValue(null);
        staffUpdatePane.setVisible(false);
    }

    @FXML
    void updateStaffBtnAction(ActionEvent event) throws IOException {
        if (webSocketAdmin != null) {
            Optional<ButtonType> optional = AlertClass.askConfirmAlert("Are you sure?");
            if (optional.isPresent() && optional.get() == ButtonType.OK) {
                String currentId = AppData.getObj().getCurrentId();
                String updateId = staffIdU.getText();
                String updateName = staffNameU.getText();
                String pw = pwU.getText();
                String nrc = nrcU.getText();
                String email = mailU.getText();
                String phone = phU.getText();
                String address = addressU.getText();

                Staff staff = null;

                if (updateStatus.equals("Inactive")) {
                    if (updateQuitDate == null) {
                        AlertClass.errorAlert("Enter Quit Date...");
                    } else {
                        staff = Database.updateStaff(con, currentId, updateId, updateName, staffType, pw, email, phone, updateDob, address, updatePhoto, nrc, updateStatus, updateStartDate, updateQuitDate);
                    }
                } else {
                    staff = Database.updateStaff(con, currentId, updateId, updateName, staffType, pw, email, phone, updateDob, address, updatePhoto, nrc, updateStatus, updateStartDate, updateQuitDate);
                }

                if (staff != null) {
                    HashMap<String, Staff> updateStaffObj = new HashMap<>();
                    updateStaffObj.put(currentId, staff);
                    byte[] bytes = convertObjectToByteBuffer(updateStaffObj);
                    webSocketAdmin.sendMessageToClient(ByteBuffer.wrap(bytes));
                }
            }
        }else
            AlertClass.errorAlert("Connect to server");
    }

    @FXML
    void staffTypeUComBoxAction(ActionEvent event) {
        staffType = staffTypeUComBox.getValue();
    }

    @FXML
    void dobUAction(ActionEvent event) {
        updateDob = String.valueOf(dobU.getValue());
    }

    @FXML
    void statusComBoxActionForStaffUpdate(ActionEvent e) {
        updateStatus = statusComBoxForStaffUpdate.getValue();
    }

    @FXML
    void startDateActionForStaffUpdate(ActionEvent event) {
        updateStartDate = String.valueOf(startDateForStaffUpdate.getValue());
    }

    @FXML
    void quitDateActionForStaffUpdate(ActionEvent event) {
        updateQuitDate = String.valueOf(quitDateForStaffUpdate.getValue());
    }

    @FXML
    void imageUpdateBtnActction(Event event) {
        File imagePath = fileChooser.showOpenDialog(new Stage());
        staffImageU.setImage(new Image("file:/" + imagePath));
        updatePhoto = imagePath.getPath();
    }

    public void setMenuPane(GridPane gp, String category_id) {
        List<AllMenu> list = AppData.getObj().getMenus();
        gp.getChildren().clear();
        int col = 0;
        int row = 1;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCategory_id().equals(category_id)) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("AdminFoodPane.fxml"));
                        Pane root = fxmlLoader.load();
                        controller = fxmlLoader.getController();
                        controller.setValue(list.get(i));

                        if (col == 6) {
                            col = 0;
                            ++row;
                        }
                        gp.add(root, col++, row);
                        GridPane.setMargin(root, new Insets(10));

                    } catch (IOException e) {
                        AlertClass.errorAlert(e.getMessage());
                    }
                }
            }
        }
    }

    /*Search bar*/
    @FXML
    private TextField searchTF;

    public void searchingMenuPane(GridPane gp, String keyword) {
        List<AllMenu> list = AppData.getObj().getMenus();
        gp.getChildren().clear();
        int col = 0;
        int row = 1;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMenu_name().toLowerCase().contains(keyword)) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("AdminFoodPane.fxml"));
                        Pane root = fxmlLoader.load();
                        controller = fxmlLoader.getController();
                        controller.setValue(list.get(i));

                        if (col == 6) {
                            col = 0;
                            ++row;
                        }

                        gp.add(root, col++, row);
                        GridPane.setMargin(root, new Insets(10));
                    } catch (IOException e) {
                        AlertClass.errorAlert(e.getMessage());
                    }
                }
            }
        }
    }


    /*Add Menu*/
    @FXML
    private ComboBox<String> categoryComBox;

    @FXML
    private ImageView menuImage;

    @FXML
    private TextField menuName;

    @FXML
    private Pane menuPane;

    @FXML
    private TextField menuUnitPrice;

    @FXML
    private TextField noOfStocks;

    private FileChooser fileChooser = new FileChooser();

    private String m_name, m_image, category_id;

    private int m_stocks;

    private double m_price;

    private List<Category> categoryList;

    @FXML
    public void addMenu(ActionEvent event) {
        staffManagementPane.setVisible(false);
        menuPane.setVisible(true);
    }

    @FXML
    void cancelAddNewMenu(ActionEvent event) {
        clearMenudata();
        menuPane.setVisible(false);
    }

    @FXML
    void categoryComBoxAction(ActionEvent event) {
        String tem = categoryComBox.getValue();
        for (int i = 0; i < categoryList.size(); i++) {
            if (tem.equals(categoryList.get(i).getName())) {
                category_id = categoryList.get(i).getId();
            }
        }
    }

    @FXML
    void confirmaddNewMenu(ActionEvent event) {
        if (webSocketAdmin != null) {
            if (takingTFData()) {
                if (category_id == null) {
                    AlertClass.errorAlert("Select Category");
                } else if (m_image == null) {
                    AlertClass.errorAlert("Select Menu Photo");
                } else {
                    Optional<ButtonType> option = AlertClass.askConfirmAlert("Are you sure?");
                    if (option.isPresent() && option.get() == ButtonType.OK) {
                        AllMenu menu = Database.addNewMenu(con, category_id, m_name, m_image, m_price, m_stocks);
                        clearMenudata();
                        menuPane.setVisible(false);
                        m_image = null;/*?*/

                        /*send new menu to server and server send menu to client*/
                        byte[] bytes = convertObjectToByteBuffer(menu);
                        webSocketAdmin.sendMessageToClient(ByteBuffer.wrap(bytes));
                    }
                }
            }
        }else
            AlertClass.errorAlert("Connect to server");
    }

    @FXML
    public void importNewMenuImage(Event event) {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            menuImage.setImage(new Image("file:/" + file.getPath()));
            m_image = file.getPath();
        }
    }

    private boolean takingTFData() {
        if (menuName.getText().isEmpty()) {
            AlertClass.errorAlert("Enter Menu Name");
            return false;
        } else if (menuUnitPrice.getText().isEmpty()) {
            AlertClass.errorAlert("Enter Unit Price");
            return false;
        } else if (noOfStocks.getText().isEmpty()) {
            AlertClass.errorAlert("Enter number of stock");
            return false;
        } else {
            m_name = menuName.getText();
            try {
                m_price = Double.parseDouble(menuUnitPrice.getText());
                m_stocks = Integer.parseInt(noOfStocks.getText());
            } catch (NumberFormatException e) {
                AlertClass.errorAlert(e.getMessage());
                return false;
            }
        }
        return true;
    }

    private void clearMenudata() {
        menuName.setText("");
        menuUnitPrice.setText("");
        noOfStocks.setText("");
        categoryComBox.setPromptText("Category");
        if (menuImage.getImage() != null) {
            menuImage.setImage(null);
        }
    }

    /*Update Menu*/
    @FXML
    private Pane updateMenuPane;

    @FXML
    private TextField um_unitPrice, m_currentStocks, um_stocks;

    @FXML
    private ImageView um_image;

    private double old_price;

    private String newU_Photo;

    @FXML
    void updateMenu(ActionEvent event) {
        if (webSocketAdmin != null) {
            List<AllMenu> list = AppData.getObj().getMenus();
            int id = AppData.getObj().getUpdate_id();
            int stocks = (um_stocks.getText().isEmpty()) ? 0 : Integer.parseInt(um_stocks.getText());
            double new_price = Double.parseDouble(um_unitPrice.getText());
            for (int i = 0; i < list.size(); i++) {
                if (id == list.get(i).getMenu_id()) {
                    old_price = list.get(i).getPrice();
                    break;
                }
            }

            if (stocks != 0 || new_price != old_price || newU_Photo != null) {
                Optional<ButtonType> optional = AlertClass.askConfirmAlert("Are you sure???");
                if (optional.isPresent() && optional.get() == ButtonType.OK) {
                    AllMenu menuObj = Database.updateMenu(con, id, stocks, new_price, newU_Photo);
                    for (int i = 0; i < list.size(); i++) {// updating menu
                        if (menuObj.getMenu_id() == list.get(i).getMenu_id()) {
                            list.get(i).setMenu_name(menuObj.getMenu_name());
                            list.get(i).setPhoto(menuObj.getPhoto());
                            list.get(i).setPrice(menuObj.getPrice());
                            list.get(i).setStocks(menuObj.getStocks());
                            /*sending menu update to waiter page*/
                            Map<Integer, AllMenu> map = new HashMap<>();
                            map.put(id, menuObj);
                            try {
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                ObjectOutputStream objOut = new ObjectOutputStream(out);
                                objOut.writeObject(map);
                                objOut.flush();
                                byte[] bytes = out.toByteArray();
                                webSocketAdmin.sendMessageToClient(ByteBuffer.wrap(bytes));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                refreshPage();
                newU_Photo = null;/*?*/
                updateMenuPane.setVisible(false);
            }
        } else {
            AlertClass.errorAlert("Connect to server");
        }
    }

    @FXML
    void cancelUpdateMenu(ActionEvent event) {
        updateMenuPane.setVisible(false);
        newU_Photo = null;/*?*/
    }

    @FXML
    public void importUpdateMenuImage(Event event) {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            um_image.setImage(new Image("file:/" + file.getPath()));
            newU_Photo = file.getPath();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*to show alert to clients when server was stopped.*/
        Stage stage = AppData.getObj().getPrimaryStage();
        stage.setOnCloseRequest(e ->{
            Server server = AppData.getObj().getServer();
            if (server != null && webSocketAdmin != null) {
                webSocketAdmin.sendDeleteMessageToClient("server was shut down");
            }
        });
        /*connect to server*/
        con = AppData.getObj().getConnection();

        menuList = AppData.getObj().getMenus();

        staffNameLbl.setText(AppData.getObj().getLoginStaffList().get(0).getName());
        webSocketAdmin = AppData.getObj().getWebSocketAdmin();
        if (webSocketAdmin == null) {
            connectToServerBtn.setText("Connect to Server");
            connectToServerBtn.setStyle("-fx-text-fill: #000");
        } else {
            connectToServerBtn.setText("Connected");
            connectToServerBtn.setStyle("-fx-text-fill: red");
        }

        AppData.getObj().setServerBtn(serverBtn);
        AppData.getObj().setConnectToServerBtn(connectToServerBtn);

        List<GridPane> gridPaneList = new ArrayList<>();
        gridPaneList.add(searchGP);
        gridPaneList.add(dishGP);
        gridPaneList.add(coolDrinkGP);
        gridPaneList.add(starterGP);
        gridPaneList.add(soupGP);

        AppData.getObj().setAdminCategoryLabel(categoryLabel);

        AppData.getObj().setMenus(menuList);
        AppData.getObj().setUIComponentForAdmin(gridPaneList, updateMenuPane, searchTF, um_unitPrice, m_currentStocks, um_stocks, um_image);

        /*for staff*/
        List<Staff> staffList = AppData.getObj().getStaffList();
        Collections.sort(staffList, new StaffComparator());

        AppData.getObj().setStaffDetailsPane(staffDetailsPane);
        AppData.getObj().setStaffUpdatePane(staffUpdatePane);
        AppData.getObj().setStaffManagementGP(staffManagementGP);

        searchPane.setVisible(true);
        menuPane.setVisible(false);
        dishPane.setVisible(false);
        staffManagementUpperPane.setVisible(false);
        staffManagementPane.setVisible(false);
        coolDrinkPane.setVisible(false);
        starterPane.setVisible(false);
        soupPane.setVisible(false);
        searchingMenuPane(searchGP, "");

        categoryLabel.setText("All Menu");

        searchTF.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (categoryLabel.getText().toLowerCase().equals("staff management")) {
                searchingStaff(newValue.toLowerCase().trim());

            } else {
                selected_pane = 0;/*?*/
                categoryLabel.setText("Searching Menu");
                AppData.getObj().setSelect_pane(0);
                searchPane.setVisible(true);
                staffManagementPane.setVisible(false);
                staffManagementUpperPane.setVisible(false);
                menuPane.setVisible(false);
                dishPane.setVisible(false);
                coolDrinkPane.setVisible(false);
                starterPane.setVisible(false);
                soupPane.setVisible(false);
                searchingMenuPane(searchGP, newValue.toLowerCase());
            }
        });

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        categoryList = AppData.getObj().getCategoryList();
        for (int i = 0; i < categoryList.size(); i++) {
            categoryComBox.getItems().add(categoryList.get(i).getName());
        }

        staffTypeAComBox.getItems().addAll("Admin", "Waiter", "Chef");
        staffTypeUComBox.getItems().addAll("Admin", "Waiter", "Chef");
        statusComBoxForStaffUpdate.getItems().addAll("Active", "Inactive");

        /*for staff details*/
        AppData.getObj().setIdLbl(staffDetailsIdLbl);
        AppData.getObj().setNameLbl(staffDetailsNameLbl);
        AppData.getObj().setStaffTypeLbl(staffDetailsTypeLbl);
        AppData.getObj().setPwLbl(staffDetailsPWLbl);
        AppData.getObj().setGenderLbl(staffDetailsGenderLbl);
        AppData.getObj().setDobLbl(staffDetailsDOBLbl);
        AppData.getObj().setNrcLbl(staffDetailsNRCLbl);
        AppData.getObj().setPhLbl(staffDetailsPhLbl);
        AppData.getObj().setEmailLbl(staffDetailsEmailLbl);
        AppData.getObj().setAddressLbl(staffDetailsAddressLbl);
        AppData.getObj().setStatusLblDetails(staffStatusForStaffDetials);
        AppData.getObj().setStartDateForDetails(startDateForStaffDetails);
        AppData.getObj().setQuitDateForDetails(quitDateForStaffDetails);
        AppData.getObj().setProfileImage(staffDetailsProfile);
        /*for staff update*/
        AppData.getObj().setStaffIdU(staffIdU);
        AppData.getObj().setStaffNameU(staffNameU);
        AppData.getObj().setStaffTypeU(staffTypeUComBox);
        AppData.getObj().setStaffPwU(pwU);
        AppData.getObj().setStaffNRCU(nrcU);
        AppData.getObj().setStaffDOBU(dobU);
        AppData.getObj().setStaffPhoneU(phU);
        AppData.getObj().setStaffEmailU(mailU);
        AppData.getObj().setStaffAddressU(addressU);
        AppData.getObj().setStatusForUpdate(statusComBoxForStaffUpdate);
        AppData.getObj().setStartDateForUpdate(startDateForStaffUpdate);
        AppData.getObj().setQuitDateForUpdate(quitDateForStaffUpdate);
        AppData.getObj().setStaffImageU(staffImageU);
    }

    public void refreshPage() {
        if (selected_pane == 0) {
            if (searchTF.getText() == null) {
                searchTF.setText("");
            }
            searchingMenuPane(searchGP, searchTF.getText().toLowerCase());
        } else {
            switch (selected_pane) {
                case 1:
                    setMenuPane(dishGP, "1");
                    break;
                case 2:
                    setMenuPane(coolDrinkGP, "2");
                    break;
                case 3:
                    setMenuPane(starterGP, "3");
                    break;
                case 4:
                    setMenuPane(soupGP, "4");
                    break;

            }
        }
    }

    public void refreshPage(List<GridPane> gridPaneList, TextField searchTF, int selected_pane) {
        if (selected_pane == 0) {
            if (searchTF.getText() == null) {
                searchTF.setText("");
            }
            searchingMenuPane(gridPaneList.get(0), searchTF.getText().toLowerCase());
        } else {
            switch (selected_pane) {
                case 1:
                    setMenuPane(gridPaneList.get(1), "1");
                    break;
                case 2:
                    setMenuPane(gridPaneList.get(2), "2");
                    break;
                case 3:
                    setMenuPane(gridPaneList.get(3), "3");
                    break;
                case 4:
                    setMenuPane(gridPaneList.get(4), "4");
                    break;
            }
        }
    }

    public void deleteMenu(List<AllMenu> list, List<GridPane> gridPaneList, TextField searchTF, int selected_pane) {
        AppData.getObj().setMenus(list);
        refreshPage(gridPaneList, searchTF, selected_pane);
    }

    @FXML
    void startServer(ActionEvent event) {
        new Thread(() -> {
            MainServer.startServer();
        }).start();
    }

    @FXML
    void connectToServer(ActionEvent event) {
        String serverIpAddress = AppData.getObj().getServerIpAddress();
        if (serverIpAddress == null) {
            serverIpAddress = Database.getServerIpAddress(AppData.getObj().getConnection());
        }

        if (AdminWebSocketClient.getSession() == null) {
            String serverURL = "ws://" + serverIpAddress.trim() + ":8080/ws/hangOutServer";
            webSocketAdmin = new WebSocketAdmin(serverURL);
            AppData.getObj().setWebSocketAdmin(webSocketAdmin);
            connectToServerBtn.setText("Connected");
            connectToServerBtn.setStyle("-fx-text-fill: red");
        }

    }

    public void showAlertServerConnectionFailed() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        AlertClass.errorAlert("Server was shut down on " + formatter.format(LocalDateTime.now()));
    }


}
