package com.group4project;

import java.io.Serializable;

public class Staff implements Serializable {
    private String id;
    private String name;
    private String staffType;
    private String password;
    private String gender;
    private String email;
    private String phone;
    private String dob;
    private String address;
    private byte[] photo;
    private String NRC;
    private String status;
    private String startDate;
    private String quitDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getNRC() {
        return NRC;
    }

    public void setNRC(String NRC) {
        this.NRC = NRC;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getQuitDate() {
        return quitDate;
    }

    public void setQuitDate(String quitDate) {
        this.quitDate = quitDate;
    }

    public Staff(String id, String name, String staffType, String password, String gender, String email, String phone, String dob, String address, byte[] photo, String NRC, String status, String startDate, String quitDate) {
        this.id = id;
        this.name = name;
        this.staffType = staffType;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.photo = photo;
        this.NRC = NRC;
        this.status = status;
        this.startDate = startDate;
        this.quitDate = quitDate;
    }
}
