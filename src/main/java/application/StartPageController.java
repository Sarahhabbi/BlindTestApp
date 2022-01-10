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
import multiplayer.client.AdminController;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartPageController implements Initializable {

    @FXML
    private Button practiceBtn;

    @FXML
    private Button multiplayerBtn;

    @FXML
    private Button quitBtn;

    //*********************************************************************************
    private Stage stage;
    private Scene scene;
    private static Parent root;

    //************************************************************************************


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //**********************************************************************************
    @FXML
    public void toPracticeMode(ActionEvent event){
        switchWindow(event, "/application/gameType.fxml", "BlindTest.IO | Game Type" );
    }

    //***********************************************************************************
    @FXML
    public void toMultiplayer(ActionEvent event){
        switchWindow(event, "/multiplayer/client/mainMultiPlayerPage.fxml", "BlindTest.IO | Multiplayer" );
    }


    //***********************************************************************************
    @FXML
    public void quitGame(ActionEvent event){
        Platform.exit();
    }

    //************************************************************************************
    public void switchWindow(ActionEvent e, String pageToLoad, String title){
        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(pageToLoad)));
            root = loader.load();


            AdminController adminController = loader.getController();
            System.out.println("switching to another page");


            /* changing the scene */
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
