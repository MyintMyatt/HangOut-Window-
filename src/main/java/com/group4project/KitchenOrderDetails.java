package com.group4project;

import java.io.Serializable;
import java.util.List;

public class KitchenOrderDetails implements Serializable {

    private List<OrderItems> orderItemsList;
    private String orderStatus;
    private int tableNo;
    private String orderTime;

    public KitchenOrderDetails(List<OrderItems> orderItemsList, String orderStatus, int tableNo, String orderTime) {
        this.orderItemsList = orderItemsList;
        this.orderStatus = orderStatus;
        this.tableNo = tableNo;
        this.orderTime = orderTime;
    }

    public List<OrderItems> getOrderItemsList() {
        return orderItemsList;
    }

    public void setOrderItemsList(List<OrderItems> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}



