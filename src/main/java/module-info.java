module com.group4project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javax.websocket.api;
    requires jasperreports;
    requires tyrus.server;
    requires java.sql;
    requires itextpdf;
    requires ecj;


    opens com.group4project to javafx.fxml;
    exports com.group4project;
}