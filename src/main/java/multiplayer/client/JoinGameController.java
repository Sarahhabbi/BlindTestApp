package multiplayer.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import multiplayer.network.ComSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class JoinGameController extends Thread implements Initializable {

    @FXML
    private TextField gameNameFieldJ;

    @FXML
    private Button goBackHome;


    /********************************************************************/

    private ComSocket comSocket;
    private boolean isAdmin;
    private boolean gameNameExists;
    private String currentPage = "joinGamePage";
    private String gameName;


    /********************************************************************/

    /*------------------------------------------------------------
     * Preparing variables to change the scene after joining
     * -----------------------------------------------------------*/
    private Stage stage;
    private Scene scene;
    private static Parent root;
    private boolean firsttime = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void storePlayerInformation(boolean isAdmin, boolean gameNameExists,ComSocket comSocket) {
        this.isAdmin = isAdmin;
        this.gameNameExists = gameNameExists;
        this.comSocket=comSocket;
        // this.start();
    }

    @FXML
    public void goBackHome(ActionEvent event){
        try {
            String title = "BlindTest.IO";
            String pageToLoad = " ";

            if(event.getSource().equals(goBackHome))
            {
                pageToLoad = "/application/homePage.fxml";
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
    public void quitFromGameJoin(ActionEvent event){
        Platform.exit();
    }

    @Override
    public void run(){
        try {
            System.out.println("JOIN");
            System.out.println("on rentre dans le thread wait for server responses ....");
            String msg = comSocket.read(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)

            while(comSocket.isConnected()==true && msg!=null &&  currentPage.equals("joinGamePage") == true) {
                if(gameNameExists == true){
                    break;
                }
                handleServerResponse(msg);
                System.out.println("JoinGame SERVER sent : " + msg);
                msg =  comSocket.read();   // prochaine message recu
            }
            System.out.println("FIN THREAD JOIN GAME CONTROLLER ");

        } catch (Exception e) {
            e.printStackTrace();
            Controller.closeEverything( comSocket);
        }
    }

    public void handleServerResponse(String msg) {
        switch(msg){

            case "/ JOINED":
                System.out.println("DEBUG / JOINED");
                this.gameNameExists = true;
                System.out.println("game exists " + gameNameExists);
                break;

            case "/ NOT_EXISTS_GAMENAME":
                System.out.println("DEBUG / NOT_EXISTS_GAMENAME");
                this.gameNameExists = false;
                System.out.println("game exists " + gameNameExists);
                break;
           default:
               System.out.println("any case join game");
               break;
        }
    }

    /* envoie au client handler "create gameName" ou "join gameName" -> need to handle clientHandler responses*/
    public void submitGameName(ActionEvent e) throws IOException {
        Window windowOwner = ((Node)e.getSource()).getScene().getWindow();
        String gameName = gameNameFieldJ.getText().toLowerCase();

        if (gameName.isEmpty() == true){
            Controller.displayAlert(Alert.AlertType.ERROR,windowOwner,"GameName vide", "Veuillez saisir un nom de partie");
            return;
        }

        if(this.firsttime==false) {
            this.start();
            this.firsttime = true;
        }

        /* player chose "join a game" */
        System.out.println("JOIN GAME CONTROLLER DEBUG isAdmin ? " + isAdmin);

        System.out.println("-> IS NOOOT ADMIN");

        comSocket.write("join " + gameName);  // envoi le pseudo au ClientHandler
        System.out.println("submitGameName()-> Sent \"join "+ gameName +"\"to Client handler");

        try {
            Thread.sleep(300);
            if(gameNameExists == false){
                Controller.displayAlert(Alert.AlertType.ERROR,windowOwner,"GameName NOT FOUND", "Veuillez saisir un nom de partie qui existe déjà");
            }
            else{
                this.gameName = gameName;
                System.out.println("REDIRECTION VERS PLAYER PAGE");
                changeWindow(e,"/multiplayer/client/playerPage.fxml", "BlindTest.IO | Multiplayer game as simple player", this.isAdmin, this.gameNameExists,this.comSocket);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    public void changeWindow(ActionEvent e, String pageToLoad, String title, boolean isAdmin, boolean gameNameExists,ComSocket comSocket){
        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(pageToLoad)));
            root = loader.load();


            /* create a page JoinGameController */
            PlayerController playerController = loader.getController();
            System.out.println("sending player info to playerController");
            playerController.storePlayerInformation(isAdmin, gameNameExists, comSocket, this.gameName);

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
