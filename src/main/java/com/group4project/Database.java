package com.group4project;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    private static Connection con = null;
//    public static Connection createConnection() {
//
//        try {
//            System.out.println("Starting to connect");
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://mysql-schprj-phkvpn-d7db.h.aivencloud.com:24048/order_and_booking_sys?ssl-mode=REQUIRED", "avnadmin", "AVNS_CO5NqwLOMTS7Km6oFcC");
//            System.out.println("DB Connected !!!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            AlertClass.errorAlert(e.getMessage());
//        }
//        return con;
//    }
    public static Connection createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CafeShopManagementSystem", "root", "");
            System.out.println("DB connected !!");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }


    public static void setServerIpAddress(Connection con, String serverIpAddress) {
        try (Statement st = con.createStatement();){
            st.executeUpdate("update ipAddress set serverIpAddress = '" + serverIpAddress + "';  ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getServerIpAddress(Connection con) {
        try (Statement st = con.createStatement();) {
            ResultSet rs = st.executeQuery("select serverIpAddress from ipAddress where primaryKey = 1 ;");
            if (rs.next()) {
                return rs.getString("serverIpAddress");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /*Staff*/
    public static List<Staff> getAllStaff(Connection con) {
        List<Staff> list = new ArrayList<>();
        if (con != null) {
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select * from staff");
                while (rs.next()) {
                    list.add(new Staff(rs.getString("staff_id"), rs.getString("staff_name"), rs.getString("staff_type"), rs.getString("password"), rs.getString("gender"), rs.getString("email"), rs.getString("phone"), rs.getString("DOB"), rs.getString("address"), rs.getBytes("photo"), rs.getString("NRC"), rs.getString("status"), rs.getString("start_date"), rs.getString("quit_date")));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return list;
    }

    public static Staff addNewStaff(Connection con, String id, String name, String type, String pw, String gender, String email, String phone, String dob, String address, String photo_fillPath, String nrc, String startDate) {
        if (con != null) {
            try (FileInputStream fin = new FileInputStream(photo_fillPath);
                 CallableStatement call = con.prepareCall("{call addNewStaff(?,?,?,?,?,?,?,?,?,?,?,?)}");) {

                call.setString(1, id);
                call.setString(2, name);
                call.setString(3, type);
                call.setString(4, pw);
                call.setString(5, gender);
                call.setString(6, email);
                call.setString(7, phone);
                call.setString(8, dob);
                call.setString(9, address);
                call.setBinaryStream(10, fin, fin.available());
                call.setString(11, nrc);
                call.setString(12, startDate);

                call.execute();
                AlertClass.confirmAlert("Successfully !!");
                return getStaff(con, id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static Staff getStaff(Connection con, String id) {
        try (Statement st = con.createStatement();){

            ResultSet rs = st.executeQuery("select * from staff where staff_id = '" + id + "'; ");
            if (rs.next()) {
                return new Staff(rs.getString("staff_id"), rs.getString("staff_name"), rs.getString("staff_type"), rs.getString("password"), rs.getString("gender"), rs.getString("email"), rs.getString("phone"), rs.getString("DOB"), rs.getString("address"), rs.getBytes("photo"), rs.getString("NRC"), rs.getString("status"), rs.getString("start_date"), rs.getString("quit_date"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Staff updateStaff(Connection con, String currentId, String updateId, String name, String staffType, String pw, String email, String phone, String dob, String address, String photo, String NRC, String status, String startDate, String quitDate) {

        try {
            CallableStatement call = null;
            if (photo == null) {
                call = con.prepareCall("{call updateStaffNotIncludedPhoto(?,?,?,?,?,?,?,?,?,?,?,?,?)};");
                call.setString(1, currentId);
                call.setString(2, updateId);
                call.setString(3, name);
                call.setString(4, staffType);
                call.setString(5, pw);
                call.setString(6, email);
                call.setString(7, phone);
                call.setString(8, dob);
                call.setString(9, address);
                call.setString(10, NRC);
                call.setString(11, status);
                call.setString(12, startDate);
                call.setString(13, quitDate);

            }

            if (photo != null) {
                FileInputStream fin = new FileInputStream(photo);
                call = con.prepareCall("{call updateStaffIncludedPhoto(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                call.setString(1, currentId);
                call.setString(2, updateId);
                call.setString(3, name);
                call.setString(4, staffType);
                call.setString(5, pw);
                call.setString(6, email);
                call.setString(7, phone);
                call.setString(8, dob);
                call.setString(9, address);
                call.setBinaryStream(10, fin, fin.available());
                call.setString(11, NRC);
                call.setString(12, status);
                call.setString(13, startDate);
                call.setString(14, quitDate);
            }
            if (call != null) {
                call.execute();
                AlertClass.confirmAlert("Successfully Updated!!");
            }

            return getStaff(con, updateId);

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Category Table*/
    public static List<Category> categoryList(Connection con) {
        List<Category> list = new ArrayList<>();
        if (con != null) {
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select * from category");
                while (rs.next()) {
                    list.add(new Category(rs.getString(1), rs.getString(2)));
                }
            } catch (SQLException e) {
                AlertClass.errorAlert(e.getMessage());
            }

        }
        return list;
    }

    /*Insert New Menu*/
    public static AllMenu addNewMenu(Connection con, String c_id, String m_name, String m_photo, double price, int stocks) {
        if (con != null) {
            try {
                FileInputStream fin = new FileInputStream(m_photo);
                CallableStatement call = con.prepareCall("{call addNewMenu(?,?,?,?,?,?)}");

                call.setString(1, c_id);
                call.setString(2, m_name);
                call.setBinaryStream(3, fin, fin.available());
                call.setDouble(4, price);
                call.setInt(5, stocks);
                call.registerOutParameter(6, Types.INTEGER);

                call.execute();

                int newMenuId = call.getInt(6);

                /*get new menu*/
                AllMenu obj = getSpecificMenu(con, newMenuId);
                AlertClass.confirmAlert("Successfully !");
                return obj;

            } catch (SQLException | IOException e) {
                AlertClass.errorAlert(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    /*For loading All menu*/
    public static List<AllMenu> getAllMenu(Connection con) {
        List<AllMenu> list = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from menu");
            while (rs.next()) {
                byte[] photo = rs.getBytes("photo");
                list.add(new AllMenu(rs.getInt("menu_id"), rs.getString("fk_category_id"), rs.getString("menu_name"), rs.getDouble("price"), rs.getInt("stocks"), photo));
            }
        } catch (SQLException e) {
            AlertClass.errorAlert(e.getMessage());

        }
        return list;
    }

    public static AllMenu getSpecificMenu(Connection con, int menuId) {
        try {
            PreparedStatement st = con.prepareCall("{call getSpecificMenu(?)};");
            st.setInt(1, menuId);
            ResultSet rs = st.executeQuery();
            int i = 0;
            while (rs.next()) {
                return new AllMenu(rs.getInt("menu_id"), rs.getString("fk_category_id"), rs.getString("menu_name"), rs.getDouble("price"), rs.getInt("stocks"), rs.getBytes("photo"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /*Update Menu*/
    public static AllMenu updateMenu(Connection con, int menu_id, int stocks, double price, String photo_filepath) {
        try {
            CallableStatement call = null;
            if (photo_filepath == null) {

                call = con.prepareCall("{call update_menu_withoutphoto(?,?,?)}");
                call.setInt(1, menu_id);
                call.setInt(2, stocks);
                call.setDouble(3, price);

            } else {
                FileInputStream fin = new FileInputStream(photo_filepath);
                call = con.prepareCall("{call update_menu_withphoto(?,?,?,?)}");
                call.setInt(1, menu_id);
                call.setInt(2, stocks);
                call.setDouble(3, price);
                call.setBinaryStream(4, fin, fin.available());

            }

            if (call != null) {
                call.execute();
                AllMenu obj = getSpecificMenu(con, menu_id);
                return obj;
            }

            AlertClass.confirmAlert("Successfully!!");
        } catch (SQLException | FileNotFoundException e) {
            AlertClass.errorAlert(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            AlertClass.errorAlert(e.getMessage());
        }
        return null;
    }

    public static void updateStock(Connection con, int menu_id, int qty) {
        try {
            PreparedStatement st = con.prepareCall("{call update_stocks(?,?)}");
            st.setInt(1, menu_id);
            st.setInt(2, qty);
            st.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<AllMenu> deleteMenu(Connection con, int menu_id) {
        List<AllMenu> list;
        try (Statement statement = con.createStatement();
             Statement st = con.createStatement();) {
            statement.executeUpdate("delete from orderDetails where fk_menu_id = '" + menu_id + "'; ");
            st.executeUpdate("delete from v_menu where menu_id = '" + menu_id + "'; ");

            list = getAllMenu(con);

            AlertClass.confirmAlert("Successfully deleted!!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<Table> getAllTables(Connection con) {
        List<Table> list = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from tbl");
            while (rs.next()) {
                list.add(new Table(rs.getInt("table_no"), rs.getInt("table_status"), rs.getString("b_name"), rs.getString("b_pw")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<Table> changeTableStatus(Connection con, int tblNo, int status) {
        List<Table> list;
        try {
            Statement st = con.createStatement();
            st.executeUpdate("update tbl set table_status = '" + status + "', b_name = '', b_pw = '' where table_no = '" + tblNo + "'; ");
            list = getAllTables(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /*For order*/
    public static int addNewOrder(Connection con, String staffId, int tableNo) {
        int newGenerateOrderId;
        try {
            CallableStatement call = con.prepareCall("{call addNewOrder(?,?,?,?)}");
            call.setString(1, staffId);
            call.setInt(2, tableNo);
            call.setString(3, "unpaid");
            call.registerOutParameter(4, Types.INTEGER);
            call.execute();

            newGenerateOrderId = call.getInt(4);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newGenerateOrderId;
    }

    public static void addOrderDetails(Connection con, int orderId, int menuId, int quantity, double unitPrice, double totalPrice) {
        try {
            CallableStatement call = con.prepareCall("{call addOrderDetails(?,?,?,?,?)}");
            call.setInt(1, orderId);
            call.setInt(2, menuId);
            call.setInt(3, quantity);
            call.setDouble(4, unitPrice);
            call.setDouble(5, totalPrice);
            call.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeOrderPaymentStatus(Connection con, int orderId) {
        try {
            Statement st = con.createStatement();
            st.executeUpdate("update orders set payment_status = 'paid' where order_no = '" + orderId + "'; ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateTotalAmount(Connection con, int orderId, double totalAmount) {
        try {
            Statement st = con.createStatement();
            st.executeUpdate("update orders set total_amount = '" + totalAmount + "' where order_no = '" + orderId + "'; ");
            AlertClass.confirmAlert("Successfully!!!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
//        Connection connection = createConnection();
//        updateStock(connection, 1, 2);
        createConnection();
    }
}
