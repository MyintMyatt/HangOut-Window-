package com.group4project;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.*;

public class AppData {

    private String serverIpAddress;

    public String getServerIpAddress() {
        return serverIpAddress;
    }

    public void setServerIpAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }

    private static AppData obj = new AppData();
    private Connection con;

    /*for admin Page*/
    private List<AllMenu> menus = new ArrayList<>();
    private List<Category> categoryList;
    private List<GridPane> gridPaneListAdmin;
    private Label adminCategoryLabel;
    private Pane updatePane;
    private TextField search_tf;
    private TextField price_tf;
    private TextField current_stock_tf;
    private TextField add_stocks_tf;
    private ImageView u_image;
    private int update_id;
    private int select_pane;


    /*for staff management*/
    private List<Staff> staffList = new ArrayList<>();
    private HBox staffDetailsPane;
    private HBox staffUpdatePane;
    private String currentId; //for staff update

    public HBox getStaffDetailsPane() {
        return staffDetailsPane;
    }

    public void setStaffDetailsPane(HBox staffDetailsPane) {
        this.staffDetailsPane = staffDetailsPane;
    }

    public HBox getStaffUpdatePane() {
        return staffUpdatePane;
    }

    public void setStaffUpdatePane(HBox staffUpdatePane) {
        this.staffUpdatePane = staffUpdatePane;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }
    /*for staff details*/
    private Label idLbl;
    private Label nameLbl;
    private Label staffTypeLbl;
    private Label pwLbl;
    private Label genderLbl;
    private Label dobLbl;
    private Label nrcLbl;
    private Label phLbl;
    private Label emailLbl;
    private Label addressLbl;
    private Label statusLblDetails;
    private Label startDateForDetails;
    private Label quitDateForDetails;
    private ImageView profileImage;

    public Label getStatusLblDetails() {
        return statusLblDetails;
    }

    public void setStatusLblDetails(Label statusLblDetails) {
        this.statusLblDetails = statusLblDetails;
    }

    public Label getStartDateForDetails() {
        return startDateForDetails;
    }

    public void setStartDateForDetails(Label startDateForDetails) {
        this.startDateForDetails = startDateForDetails;
    }

    public Label getQuitDateForDetails() {
        return quitDateForDetails;
    }

    public void setQuitDateForDetails(Label quitDateForDetails) {
        this.quitDateForDetails = quitDateForDetails;
    }

    public Label getIdLbl() {
        return idLbl;
    }

    public void setIdLbl(Label idLbl) {
        this.idLbl = idLbl;
    }

    public Label getNameLbl() {
        return nameLbl;
    }

    public void setNameLbl(Label nameLbl) {
        this.nameLbl = nameLbl;
    }

    public Label getStaffTypeLbl() {
        return staffTypeLbl;
    }

    public void setStaffTypeLbl(Label staffTypeLbl) {
        this.staffTypeLbl = staffTypeLbl;
    }

    public Label getPwLbl() {
        return pwLbl;
    }

    public void setPwLbl(Label pwLbl) {
        this.pwLbl = pwLbl;
    }

    public Label getGenderLbl() {
        return genderLbl;
    }

    public void setGenderLbl(Label genderLbl) {
        this.genderLbl = genderLbl;
    }

    public Label getDobLbl() {
        return dobLbl;
    }

    public void setDobLbl(Label dobLbl) {
        this.dobLbl = dobLbl;
    }

    public Label getNrcLbl() {
        return nrcLbl;
    }

    public void setNrcLbl(Label nrcLbl) {
        this.nrcLbl = nrcLbl;
    }

    public Label getPhLbl() {
        return phLbl;
    }

    public void setPhLbl(Label phLbl) {
        this.phLbl = phLbl;
    }

    public Label getEmailLbl() {
        return emailLbl;
    }

    public void setEmailLbl(Label emailLbl) {
        this.emailLbl = emailLbl;
    }

    public Label getAddressLbl() {
        return addressLbl;
    }

    public void setAddressLbl(Label addressLbl) {
        this.addressLbl = addressLbl;
    }

    public ImageView getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ImageView profileImage) {
        this.profileImage = profileImage;
    }

    /*for staff update*/
    private TextField staffIdU;
    private TextField staffNameU;
    private ComboBox<String> staffTypeU;
    private TextField staffPwU;
    private TextField staffNRCU;
    private DatePicker staffDOBU;
    private TextField staffPhoneU;
    private TextField staffEmailU;
    private TextArea staffAddressU;
    private ImageView staffImageU;
    private ComboBox<String> statusForUpdate;
    private DatePicker startDateForUpdate;
    private DatePicker quitDateForUpdate;

    public ComboBox<String> getStatusForUpdate() {
        return statusForUpdate;
    }

    public void setStatusForUpdate(ComboBox<String> statusForUpdate) {
        this.statusForUpdate = statusForUpdate;
    }

    public DatePicker getStartDateForUpdate() {
        return startDateForUpdate;
    }

    public void setStartDateForUpdate(DatePicker startDateForUpdate) {
        this.startDateForUpdate = startDateForUpdate;
    }

    public DatePicker getQuitDateForUpdate() {
        return quitDateForUpdate;
    }

    public void setQuitDateForUpdate(DatePicker quitDateForUpdate) {
        this.quitDateForUpdate = quitDateForUpdate;
    }

    public ImageView getStaffImageU() {
        return staffImageU;
    }

    public void setStaffImageU(ImageView staffImageU) {
        this.staffImageU = staffImageU;
    }

    public TextArea getStaffAddressU() {
        return staffAddressU;
    }

    public void setStaffAddressU(TextArea staffAddressU) {
        this.staffAddressU = staffAddressU;
    }

    public TextField getStaffEmailU() {
        return staffEmailU;
    }

    public void setStaffEmailU(TextField staffEmailU) {
        this.staffEmailU = staffEmailU;
    }

    public TextField getStaffPhoneU() {
        return staffPhoneU;
    }

    public void setStaffPhoneU(TextField staffPhoneU) {
        this.staffPhoneU = staffPhoneU;
    }

    public DatePicker getStaffDOBU() {
        return staffDOBU;
    }

    public void setStaffDOBU(DatePicker staffDOBU) {
        this.staffDOBU = staffDOBU;
    }

    public TextField getStaffNRCU() {
        return staffNRCU;
    }

    public void setStaffNRCU(TextField staffNRCU) {
        this.staffNRCU = staffNRCU;
    }

    public TextField getStaffPwU() {
        return staffPwU;
    }

    public void setStaffPwU(TextField staffPwU) {
        this.staffPwU = staffPwU;
    }

    public ComboBox<String> getStaffTypeU() {
        return staffTypeU;
    }

    public void setStaffTypeU(ComboBox<String> staffTypeU) {
        this.staffTypeU = staffTypeU;
    }

    public TextField getStaffNameU() {
        return staffNameU;
    }

    public void setStaffNameU(TextField staffNameU) {
        this.staffNameU = staffNameU;
    }

    public TextField getStaffIdU() {
        return staffIdU;
    }

    public void setStaffIdU(TextField staffIdU) {
        this.staffIdU = staffIdU;
    }

    /*for table pane*/
    private List<Table> tableList;
    private Pane confirmBookingPane;
    private String tableNumber;
    private Label customerName;
    private Label tblNumberLabel;
    private Stage stage;

    /*for waiter page*/
    private Label total_amount;
    private ListView listView;
    private Label s_Page_tblNumber;
    private List<LoginStaff> loginStaffList;
    private List<GridPane> gridPaneListWaiter;
    private Label waiterCategoryLabel;

    public List<GridPane> getGridPaneListWaiter() {
        return gridPaneListWaiter;
    }

    public void setGridPaneListWaiter(List<GridPane> gridPaneListWaiter) {
        this.gridPaneListWaiter = gridPaneListWaiter;
    }

    public Label getWaiterCategoryLabel() {
        return waiterCategoryLabel;
    }


    public void setWaiterCategoryLabel(Label categoryLabel) {
        this.waiterCategoryLabel = categoryLabel;
    }

    public List<LoginStaff> getLoginStaffList() {
        return loginStaffList;
    }

    public void setLoginStaffList(List<LoginStaff> loginStaffList) {
        this.loginStaffList = loginStaffList;
    }

    /*for admin page*/
    public void setUIComponentForAdmin(List<GridPane> gridPaneList, Pane updatePane, TextField search_tf, TextField price_tf, TextField current_stock_tf, TextField add_stocks_tf, ImageView u_image) {
        this.gridPaneListAdmin = gridPaneList;
        this.updatePane = updatePane;
        this.search_tf = search_tf;
        this.price_tf = price_tf;
        this.current_stock_tf = current_stock_tf;
        this.add_stocks_tf = add_stocks_tf;
        this.u_image = u_image;
    }

    public static AppData getObj() {
        return obj;
    }

    public Connection getConnection() {
        return con;
    }

    public void setConnection(Connection con) {
        this.con = con;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<AllMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<AllMenu> menus) {
        this.menus = menus;
    }

    public List<GridPane> getGridPaneListAdmin() {
        return gridPaneListAdmin;
    }

    public TextField getSearch_tf() {
        return search_tf;
    }

    public TextField getPrice_tf() {
        return price_tf;
    }

    public TextField getCurrent_stock_tf() {
        return current_stock_tf;
    }

    public TextField getAdd_stocks_tf() {
        return add_stocks_tf;
    }

    public Pane getUpdatePane() {
        return updatePane;
    }

    public ImageView getU_image() {
        return u_image;
    }

    public int getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(int update_id) {
        this.update_id = update_id;
    }

    public int getSelect_pane() {
        return select_pane;
    }

    public void setSelect_pane(int select_pane) {
        this.select_pane = select_pane;
    }

    public Label getAdminCategoryLabel() {
        return adminCategoryLabel;
    }

    public void setAdminCategoryLabel(Label adminCategoryLabel) {
        this.adminCategoryLabel = adminCategoryLabel;
    }

    /*for staff management*/

    private GridPane staffManagementGP;

    public GridPane getStaffManagementGP() {
        return staffManagementGP;
    }

    public void setStaffManagementGP(GridPane staffManagementGP) {
        this.staffManagementGP = staffManagementGP;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }


    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }


    /*for table pane*/
    public List<Table> getTableList() {
        return tableList;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    public Pane getConfirmBookingPane() {
        return confirmBookingPane;
    }

    public void setConfirmBookingPaneElements(Pane confirmBookingPane, Label customerName, Label tblNumberLabel) {
        this.confirmBookingPane = confirmBookingPane;
        this.customerName = customerName;
        this.tblNumberLabel = tblNumberLabel;
    }

    public Label getCustomerName() {
        return customerName;
    }

    public Label getTblNumberLabel() {
        return tblNumberLabel;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }


    /*for waiter page*/

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public Label getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Label total_amount) {
        this.total_amount = total_amount;
    }

    public void setS_Page_tblNumber(Label s_Page_tblNumber) {
        this.s_Page_tblNumber = s_Page_tblNumber;
    }

    public Label getS_Page_tblNumber() {
        return s_Page_tblNumber;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getPrimaryStage() {
        return stage;
    }

    /*for reload order items*/
    private Map<Integer, Integer> orderMap = new HashMap<>();

    public Map<Integer, Integer> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<Integer, Integer> orderMap) {
        this.orderMap = orderMap;
    }

    /*for new menu items in listView*/
    private Map<Integer, Integer> rowMap = new HashMap<>();

    public Map<Integer, Integer> getRowMap() {
        return rowMap;
    }

    public void setRowMap(Map<Integer, Integer> rowMap) {
        this.rowMap = rowMap;
    }

    /*data storing for kitchen order*/

    private Map<String, List<KitchenOrderDetails>> kitchenOrderDetailsMap = new LinkedHashMap<>();

    public Map<String, List<KitchenOrderDetails>> getKitchenOrderDetailsMap() {
        return kitchenOrderDetailsMap;
    }

    /*for kitchen UI component*/

    private GridPane orderGP;

    public GridPane getOrderGP() {
        return orderGP;
    }

    public void setOrderGP(GridPane orderGP) {
        this.orderGP = orderGP;
    }

    private Label orderStatusLbl;

    public Label getOrderStatusLbl() {
        return orderStatusLbl;
    }

    public void setOrderStatusLbl(Label orderStatusLbl) {
        this.orderStatusLbl = orderStatusLbl;
    }

    private WebSocketAdmin webSocketAdmin;

    public WebSocketAdmin getWebSocketAdmin() {
        return webSocketAdmin;
    }

    public void setWebSocketAdmin(WebSocketAdmin webSocketAdmin) {
        this.webSocketAdmin = webSocketAdmin;
    }

    private WebSocketClient webSocketClient;

    public WebSocketClient getWebSocketClient() {
        return webSocketClient;
    }

    public void setWebSocketClient(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    /*for update stock*/
    private String menuName;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
