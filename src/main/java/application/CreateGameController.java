package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreateGameController implements Initializable {


    @FXML
    private TextField usernameFieldC;

    @FXML
    private TextField gameNameFieldC;

    @FXML
    private Button goBackHome;

    @FXML
    private Button quitFromGameCreation;

    /*------------------------------------------------------------
     * Preparing variables to change the scene after login
     * -----------------------------------------------------------*/
    private Stage stage;
    private Scene scene;
    private static Parent root;


    @FXML
    public void goBackHome(ActionEvent event) throws IOException {
        try {
            String title = "BlindTest.IO";
            String pageToLoad = " ";

            if(event.getSource().equals(goBackHome))
            {
                pageToLoad = "/application/startPage.fxml";
            }

            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(pageToLoad)));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            System.out.println("fin debug");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //exit the app
    @FXML
    public void quitFromGameCreation(ActionEvent event){
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
