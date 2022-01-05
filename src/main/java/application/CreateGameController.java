package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
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






    //exit the app
    @FXML
    public void quitFromGameCreation(ActionEvent event){
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
