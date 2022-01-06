package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.MyImage;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class BlindTestApplication extends Application {
    public static int ROUND = 2;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        FXMLLoader fxmlLoader = new FXMLLoader(BlindTestApplication.class.getResource("/application/AppView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("BlindTest.IO | HomePage!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}