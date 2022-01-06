package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;

public class BlindTestApplication extends Application {

    public static int ROUND = 5;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        FXMLLoader fxmlLoader = new FXMLLoader(BlindTestApplication.class.getResource("/application/musicView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("BlindTest.IO | App View!");
        stage.setScene(scene);
        stage.show();

       /* new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                String bip = "/Users/habbi/Documents/L3-DANT/PROJETS_GROUPE/BlindTestApp/src/main/resources/music/bruno-mars-talking-to-the-moon-lyrics.mp3";
                Media hit = new Media(new File(bip).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.play();
            }
        }).start();*/
    }

    public static void main(String[] args) {

        launch();
    }
}