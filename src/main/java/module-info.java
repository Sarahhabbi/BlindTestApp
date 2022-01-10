module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires junit;
    requires se.michaelthelin.spotify;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires nv.i18n;
    requires jlayer;
    requires jaco.mp3.player;


    opens application to javafx.fxml;
    exports application;

    opens multiplayer.client to javafx.fxml;
    exports multiplayer.client;
}