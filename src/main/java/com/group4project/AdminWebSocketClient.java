package com.group4project;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;

import javax.websocket.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.*;

@ClientEndpoint
public class AdminWebSocketClient {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("admin connected to server : " + session.getId());
    }

    @OnMessage
    public void onMessage(ByteBuffer message) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(message.array());
            ObjectInputStream objIn = new ObjectInputStream(in);
            Object object = objIn.readObject();
            List<AllMenu> list = AppData.getObj().getMenus();
            AdminPageController obj = new AdminPageController();
            GridPane staffGP = AppData.getObj().getStaffManagementGP();
            /*for stock*/
            if (object instanceof Map<?, ?>) {
                try {
                    Map<Integer, Integer> stockMap = (Map<Integer, Integer>) object;
                    Set<Integer> keySet = stockMap.keySet();
                    Iterator<Integer> iterator = keySet.iterator();
                    while (iterator.hasNext()) {
                        int menuId = iterator.next();
                        for (int i = 0; i < list.size(); i++) {
                            if (menuId == list.get(i).getMenu_id()) {
                                int oldStock = list.get(i).getStocks();
                                list.get(i).setStocks(oldStock - stockMap.get(menuId));
                                break;
                            }
                        }
                    }
                    Platform.runLater(() -> {
                        List<GridPane> gridPaneList = AppData.getObj().getGridPaneListAdmin();
                        Label categoryLbl = AppData.getObj().getAdminCategoryLabel();
                        String category = categoryLbl.getText().trim();

                        /*to update current stocks textField when update menu pane is visible */
                        TextField current_stocks_tf = AppData.getObj().getCurrent_stock_tf();
                        String menuName = AppData.getObj().getMenuName();
                        if (menuName != null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (menuName.equals(list.get(i).getMenu_name())) {
                                    int currentStocks = list.get(i).getStocks();
                                    current_stocks_tf.setText("Current Stocks : " + currentStocks);
                                    break;
                                }
                            }
                        }
                        switch (category.toLowerCase()) {
                            case "all menu":
                            case "searching menu":
                                obj.searchingMenuPane(gridPaneList.get(0), "");
                                break;
                            case "main dishes":
                                obj.setMenuPane(gridPaneList.get(1), "1");
                                break;
                            case "drinks":
                                obj.setMenuPane(gridPaneList.get(2), "2");
                                break;
                            case "starters":
                                obj.setMenuPane(gridPaneList.get(3), "3");
                                break;
                            case "soups":
                                obj.setMenuPane(gridPaneList.get(4), "4");
                                break;
                        }
                    });
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            /*for new menu*/
            if (object instanceof AllMenu) {
                try {
                    AllMenu menu = (AllMenu) object;
                    AppData.getObj().getMenus().add(menu);
                    Platform.runLater(()-> {
                        obj.refreshPage(AppData.getObj().getGridPaneListAdmin(), AppData.getObj().getSearch_tf(), AppData.getObj().getSelect_pane());
                    });
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            /*for menu update*/
            if (object instanceof Map<?, ?>) {
                try {
                    Map<Integer, AllMenu> map = (Map<Integer, AllMenu>) object;
                    Set<Integer> keySet = map.keySet();
                    Iterator<Integer> iterator = keySet.iterator();
                    int id = iterator.next();
                    AllMenu menuObj = map.get(id);
                    List<AllMenu> allMenuList = AppData.getObj().getMenus();
                    for (int i = 0; i < allMenuList.size(); i++) {
                        if (id == allMenuList.get(i).getMenu_id()) {
                            allMenuList.get(i).setStocks(menuObj.getStocks());
                            allMenuList.get(i).setPrice(menuObj.getPrice());
                            allMenuList.get(i).setPhoto(menuObj.getPhoto());
                            obj.refreshPage(AppData.getObj().getGridPaneListAdmin(), AppData.getObj().getSearch_tf(), AppData.getObj().getSelect_pane());
                            break;
                        }
                    }
                    Platform.runLater(() -> {
                        AppData.getObj().getU_image().setImage(new Image(new ByteArrayInputStream(menuObj.getPhoto())));
                        AppData.getObj().getCurrent_stock_tf().setText("Current Stocks : " + menuObj.getStocks());
                        AppData.getObj().getPrice_tf().setText(String.valueOf(menuObj.getPrice()));
                    });
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            /*for new staff*/
            if (object instanceof Staff) {

                try {
                    Staff staff = (Staff) object;
                    AppData.getObj().getStaffList().add(staff);
                    Platform.runLater(() -> {
                        obj.loadingStaffPane(staffGP);
                    });
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            /*for staff update*/
            if (object instanceof HashMap<?, ?>) {
                try {
                    List<Staff> staffList = AppData.getObj().getStaffList();
                    HashMap<String, Staff> updateStaffObj = (HashMap<String, Staff>) object;
                    Set<String> keySet = updateStaffObj.keySet();
                    Iterator<String> iterator = keySet.iterator();
                    String currentId = iterator.next();
                    Staff staff = updateStaffObj.get(currentId);
                    Platform.runLater(() -> {
                        int i = 0;
                        boolean flag = true;
                        while (flag && i < staffList.size()) {
                            if (currentId.equals(staffList.get(i).getId())) {
                                staffList.get(i).setId(staff.getId());
                                staffList.get(i).setName(staff.getName());
                                staffList.get(i).setStaffType(staff.getStaffType());
                                staffList.get(i).setPassword(staff.getPassword());
                                staffList.get(i).setEmail(staff.getEmail());
                                staffList.get(i).setPhone(staff.getPhone());
                                staffList.get(i).setDob(staff.getDob());
                                staffList.get(i).setAddress(staff.getAddress());
                                staffList.get(i).setPhoto(staff.getPhoto());
                                staffList.get(i).setNRC(staff.getNRC());
                                staffList.get(i).setStatus(staff.getStatus());
                                staffList.get(i).setStartDate(staff.getStartDate());
                                staffList.get(i).setQuitDate(staff.getQuitDate());
                                flag = false;

                                obj.loadingStaffPane(staffGP);
                                /*for staff details*/
                                AppData.getObj().getIdLbl().setText(staff.getId());
                                AppData.getObj().getNameLbl().setText(staff.getName());
                                AppData.getObj().getStaffTypeLbl().setText(staff.getStaffType());
                                AppData.getObj().getPwLbl().setText(staff.getPassword());
                                AppData.getObj().getGenderLbl().setText(staff.getGender());
                                AppData.getObj().getDobLbl().setText(staff.getDob());
                                AppData.getObj().getNrcLbl().setText(staff.getNRC());
                                AppData.getObj().getPhLbl().setText(staff.getPhone());
                                AppData.getObj().getEmailLbl().setText(staff.getEmail());
                                AppData.getObj().getAddressLbl().setText(staff.getAddress());
                                Label statusLbl = AppData.getObj().getStatusLblDetails();
                                if (staff.getStatus().equals("Inactive")) {
                                    statusLbl.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                                }else {
                                    statusLbl.setStyle("-fx-text-fill: #00df82; -fx-font-weight: bold;");
                                }
                                statusLbl.setText(staff.getStatus());
                                AppData.getObj().getStartDateForDetails().setText(staff.getStartDate());
                                AppData.getObj().getQuitDateForDetails().setText((staff.getQuitDate() != null)? staff.getQuitDate(): ".......");
                                /*for staff  update*/
                                AppData.getObj().getStaffIdU().setText(staff.getId());
                                AppData.getObj().getStaffNameU().setText(staff.getName());
                                AppData.getObj().getStaffTypeU().setValue(staff.getStaffType());
                                AppData.getObj().getStaffPwU().setText(staff.getPassword());
                                AppData.getObj().getStaffNRCU().setText(staff.getNRC());
                                LocalDate localDate = LocalDate.parse(staff.getDob());
                                AppData.getObj().getStaffDOBU().setValue(localDate);
                                AppData.getObj().getStaffPhoneU().setText(staff.getPhone());
                                AppData.getObj().getStaffEmailU().setText(staff.getEmail());
                                AppData.getObj().getStaffAddressU().setText(staff.getAddress());
                                AppData.getObj().getStatusForUpdate().setValue(staff.getStatus());
                                AppData.getObj().getStartDateForUpdate().setValue(LocalDate.parse(staff.getStartDate()));
                                LocalDate quitDate = (staff.getQuitDate() != null) ? LocalDate.parse(staff.getQuitDate()) : null;
                                AppData.getObj().getQuitDateForUpdate().setValue(quitDate);
                                InputStream input = new ByteArrayInputStream(staff.getPhoto());
                                AppData.getObj().getStaffImageU().setImage(new Image(input));
                            }
                            i++;
                        }
                    });
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            List<Staff> staffList = AppData.getObj().getStaffList();
            int i = 0;
            boolean flag = true;
            while (flag && i < staffList.size()) {
                if (message.equals(staffList.get(i).getId())) {
                    staffList.remove(i);
                    flag = false;
                }
                i++;
            }
            Platform.runLater(() -> {
                AdminPageController obj = new AdminPageController();
                GridPane gp = AppData.getObj().getStaffManagementGP();
                obj.loadingStaffPane(gp);
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("admin disconnected from server : " + session.getId());
    }

}
