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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    //Binding the fxml elements with the controller class
    @FXML
    private Button createGameBtn;

    @FXML
    private Button joinGameBtn;

    @FXML
    private Button quitFromHome;

    /*------------------------------------------------------------
     * Preparing variables to change the scene after login
     * -----------------------------------------------------------*/
    private Stage stage;
    private Scene scene;
    private static Parent root;



    @FXML
    public void homeToGameCreation(ActionEvent event) throws IOException {
        try {
            String title = "BlindTest.IO";
            String pageToLoad = " ";
            if(event.getSource().equals(createGameBtn))
            {
                pageToLoad = "/application/createGamePage.fxml";
                title = ("BlindTest.IO | Create a game");
            }
            else if(event.getSource().equals(joinGameBtn))
            {
                pageToLoad = "/multiplayer/client/joinGamePage.fxml";
                title = ("BlindTest.IO | Join a game");
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

    @FXML
    public void homeToGameJoin(ActionEvent event) throws IOException {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/joinGamePage.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("BlindTest.IO | Join a Game");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //exit the app
    @FXML
    public void quitFromHome(ActionEvent event){
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
