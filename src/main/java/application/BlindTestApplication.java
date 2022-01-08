package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class BlindTestApplication extends Application {

    public static int ROUND = 4;

    @Override
    public void start(Stage stage) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(BlindTestApplication.class.getResource("/multiplayer/client/mainMultiplayerPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("BlindTest.IO | App View!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}