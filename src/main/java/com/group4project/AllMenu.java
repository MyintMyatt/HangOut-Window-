package com.group4project;

import java.io.Serializable;

public class AllMenu implements Serializable {
    private int menu_id;
    private String category_id;
    private String menu_name;
    private double price;
    private int stocks;
    private byte[] photo;

    public int getMenu_id() {
        return menu_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public double getPrice() {
        return price;
    }

    public int getStocks() {
        return stocks;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public AllMenu(int menu_id, String category_id, String menu_name, double price, int stocks, byte[] photo) {
        this.menu_id = menu_id;
        this.category_id = category_id;
        this.menu_name = menu_name;
        this.price = price;
        this.stocks = stocks;
        this.photo = photo;
    }

}
