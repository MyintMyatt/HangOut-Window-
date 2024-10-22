package com.group4project;

import java.util.Objects;

public class Table {
    private int number;
    private int status;
    private String booking_name = "";
    private String booking_pass = "";

    public Table(int number, int status, String booking_name, String booking_pass) {
        this.number = number;
        this.status = status;
        this.booking_name = booking_name;
        this.booking_pass = booking_pass;
    }

    public int getTableNumber() {
        return number;
    }

    public void setTableStatus(int status) {
        this.status = status;
    }

    public int getTableStatus() {
        return status;
    }

    public String getBooking_name() {
        return booking_name;
    }

    public String getBooking_pass() {
        return booking_pass;
    }

    @Override
    public boolean equals(Object obj) {
        Table table = (Table) obj;
        if (this.booking_name != null && table.getBooking_name() != null && this.booking_pass != null && table.getBooking_pass() != null) {
            return this.getTableNumber() == table.getTableNumber() && this.getTableStatus() == table.getTableStatus() && this.getBooking_name().equals(table.getBooking_name()) && this.getBooking_pass().equals(table.getBooking_pass());
        }
        return this.getTableNumber() == table.getTableNumber() && this.getTableStatus() == table.getTableStatus();
    }

    @Override
    public int hashCode() {
        System.out.println(Objects.hash(number, status, booking_name, booking_pass));
        return Objects.hash(number, status, booking_name, booking_pass);
    }
}
