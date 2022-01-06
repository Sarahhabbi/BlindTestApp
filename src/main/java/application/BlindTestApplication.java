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
    public static ArrayList<MyImage> images = new ArrayList<>();
    public static int ROUND = 2;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        MyImage img1 = new MyImage("1", "https://miro.medium.com/max/1400/1*4Ha05TwcqYA1s0BG2FQPJg.jpeg", "jennifer lawrence");
        MyImage img2 = new MyImage("2", "https://miro.medium.com/max/9012/1*Ffd2A4DGyn6y0NaC7eNzlQ.jpeg", "cristiano ronaldo");
        images.add(img1);
        images.add(img2);

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