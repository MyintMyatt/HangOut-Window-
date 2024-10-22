package com.group4project;

public class OrderDetails {

    private int menuId;
    private int quantity;
    private double unitPice;
    private double totalPrice;

    public OrderDetails(int menuId, int quantity, double unitPice, double totalPrice) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.unitPice = unitPice;
        this.totalPrice = totalPrice;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPice() {
        return unitPice;
    }

    public void setUnitPice(double unitPice) {
        this.unitPice = unitPice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
