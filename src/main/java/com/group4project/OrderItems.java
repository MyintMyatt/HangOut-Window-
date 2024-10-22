package com.group4project;


import java.io.Serializable;

/*order items for per order*/
public class OrderItems implements Serializable {
    private String menuName;
    private int qty;


    public OrderItems(String menuName, int qty) {
        this.menuName = menuName;
        this.qty = qty;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
