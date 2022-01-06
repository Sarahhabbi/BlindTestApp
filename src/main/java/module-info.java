module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;




    requires javafx.graphics;
    requires java.sql;

    opens application to javafx.fxml;
    exports application;
}