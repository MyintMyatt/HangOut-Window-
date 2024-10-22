package com.group4project;

public class UpdateStocks {
    private String menuName;
    private String orderQty;

    public UpdateStocks(String menuName, String orderQty) {
        this.menuName = menuName;
        this.orderQty = orderQty;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getOrderQty() {
        return orderQty;
    }
}
