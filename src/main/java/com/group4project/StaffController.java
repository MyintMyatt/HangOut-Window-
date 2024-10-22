package com.group4project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffController implements Initializable {

    @FXML
    private Pane staffPane;

    @FXML
    private Label idLbl;

    @FXML
    private Button detailsBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Label nameLbl;

    @FXML
    private Label staffTypeLbl;

    @FXML
    private Label emailLbl;

    @FXML
    private Label phNoLbl;

    private HBox detailsStaffPane, updateStaffPane;

    private Label detailsIdLbl, detailsNameLbl, detailsStaffTypeLbl, detailsPwLbl, detailsGenderLbl, detailsDOBLbl, detailsNRCLbl, detailsPhLbl, detailsEmailLbl, detailsStatusLbl, detailsStartDateLbl, detailsQuitDateLbl, detailsAddressLbl;


    private ImageView detailsImageView;

    private TextField idTF, nameTF, pwTF, nrcTF, phTF, mailTF;

    private ComboBox<String> staffTypeComBox;

    private DatePicker dobDP;

    private ImageView imageView;

    private TextArea addressTF;

    private ComboBox<String> statusComBoxForUpdate;

    private DatePicker startDateForUpdate, quitDateForUpdate;


    @FXML
    void detailsBtnAction(ActionEvent event) {
        detailsStaffPane.setVisible(true);
        String id = idLbl.getText();
        List<Staff> list = AppData.getObj().getStaffList();
        for (int i = 0; i < list.size(); i++) {
            if (id.equals(list.get(i).getId())) {
                detailsIdLbl.setText(list.get(i).getId());
                detailsNameLbl.setText(list.get(i).getName());
                detailsStaffTypeLbl.setText(list.get(i).getStaffType());
                detailsPwLbl.setText(list.get(i).getPassword());
                detailsGenderLbl.setText(list.get(i).getGender());
                detailsDOBLbl.setText(list.get(i).getDob());
                detailsNRCLbl.setText(list.get(i).getNRC());
                detailsPhLbl.setText(list.get(i).getPhone());
                detailsEmailLbl.setText(list.get(i).getEmail());
                if (list.get(i).getStatus().equals("Inactive")) {
                    detailsStatusLbl.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                }else {
                    detailsStatusLbl.setStyle("-fx-text-fill: #00df82; -fx-font-weight: bold;");
                }
                detailsStatusLbl.setText(list.get(i).getStatus());
                detailsStartDateLbl.setText(list.get(i).getStartDate());
                String quitDate = (list.get(i).getQuitDate() != null) ? list.get(i).getQuitDate() : ".........";
                detailsQuitDateLbl.setText(quitDate);
                detailsAddressLbl.setText(list.get(i).getAddress());
                InputStream in = new ByteArrayInputStream(list.get(i).getPhoto());
                detailsImageView.setImage(new Image(in));
                break;
            }
        }
    }

    @FXML
    void updateBtnAction(ActionEvent event) {
        List<Staff> staffList = AppData.getObj().getStaffList();
        if (updateBtn.getText().toLowerCase().equals("update")) {
            updateStaffPane.setVisible(true);
            String currentId = idLbl.getText();
            AppData.getObj().setCurrentId(currentId);

            for (int i = 0; i < staffList.size(); i++) {
                if (currentId.equals(staffList.get(i).getId())) {
                    idTF.setText(currentId);
                    nameTF.setText(staffList.get(i).getName());
                    staffTypeComBox.setValue(staffList.get(i).getStaffType());
                    pwTF.setText(staffList.get(i).getPassword());
                    nrcTF.setText(staffList.get(i).getNRC());
                    if (staffList.get(i).getDob() != null) {
                        LocalDate localDate = LocalDate.parse(staffList.get(i).getDob());
                        dobDP.setValue(localDate);
                    }
                    phTF.setText(staffList.get(i).getPhone());
                    mailTF.setText(staffList.get(i).getEmail());
                    addressTF.setText(staffList.get(i).getAddress());
                    InputStream inputStream = new ByteArrayInputStream(staffList.get(i).getPhoto());
                    statusComBoxForUpdate.setValue(staffList.get(i).getStatus());
                    startDateForUpdate.setValue(LocalDate.parse(staffList.get(i).getStartDate()));
                    LocalDate quitDate = (staffList.get(i).getQuitDate() != null)? LocalDate.parse( staffList.get(i).getQuitDate()) : null;
                    quitDateForUpdate.setValue(quitDate);
                    imageView.setImage(new Image(inputStream));
                }
            }
        }else if (updateBtn.getText().toLowerCase().equals("quited")){
            String quitDate = "";
            for (int i = 0; i < staffList.size(); i++) {
                if (idLbl.getText().equals(staffList.get(i).getId())) {
                    quitDate = staffList.get(i).getQuitDate();
                    break;
                }
            }
            AlertClass.confirmAlert(nameLbl.getText() + " resigned in " + quitDate);
        }
    }

    public void setValue(Staff staff) {
        idLbl.setText(staff.getId());
        nameLbl.setText(staff.getName());
        staffTypeLbl.setText(staff.getStaffType());
        phNoLbl.setText(staff.getPhone());
        emailLbl.setText(staff.getEmail());
        if (staff.getStatus().equals("Inactive")) {
            updateBtn.setStyle("-fx-background-color: red;");
            updateBtn.setText("Quited");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailsStaffPane = AppData.getObj().getStaffDetailsPane();
        updateStaffPane = AppData.getObj().getStaffUpdatePane();

        /*for staff details*/
        detailsIdLbl = AppData.getObj().getIdLbl();
        detailsNameLbl = AppData.getObj().getNameLbl();
        detailsStaffTypeLbl = AppData.getObj().getStaffTypeLbl();
        detailsPwLbl = AppData.getObj().getPwLbl();
        detailsGenderLbl = AppData.getObj().getGenderLbl();
        detailsDOBLbl = AppData.getObj().getDobLbl();
        detailsNRCLbl = AppData.getObj().getNrcLbl();
        detailsPhLbl = AppData.getObj().getPhLbl();
        detailsEmailLbl = AppData.getObj().getEmailLbl();
        detailsStatusLbl = AppData.getObj().getStatusLblDetails();
        detailsStartDateLbl = AppData.getObj().getStartDateForDetails();
        detailsQuitDateLbl = AppData.getObj().getQuitDateForDetails();
        detailsAddressLbl = AppData.getObj().getAddressLbl();
        detailsImageView = AppData.getObj().getProfileImage();
        /*for staff update*/
        idTF = AppData.getObj().getStaffIdU();
        nameTF = AppData.getObj().getStaffNameU();
        staffTypeComBox = AppData.getObj().getStaffTypeU();
        pwTF = AppData.getObj().getStaffPwU();
        nrcTF = AppData.getObj().getStaffNRCU();
        dobDP = AppData.getObj().getStaffDOBU();
        phTF = AppData.getObj().getStaffPhoneU();
        mailTF = AppData.getObj().getStaffEmailU();
        addressTF = AppData.getObj().getStaffAddressU();
        statusComBoxForUpdate = AppData.getObj().getStatusForUpdate();
        startDateForUpdate = AppData.getObj().getStartDateForUpdate();
        quitDateForUpdate = AppData.getObj().getQuitDateForUpdate();
        imageView = AppData.getObj().getStaffImageU();
    }
}
