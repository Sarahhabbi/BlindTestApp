package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class JoinGameController implements Initializable {

    @FXML
    private TextField usernameFieldJ;

    @FXML
    private TextField gameNameFieldJ;

    @FXML
    private Button goBackHome;

    @FXML
    private Button quitFromGameJoin;





    //exit the app
    @FXML
    public void quitFromGameJoin(ActionEvent event){
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
