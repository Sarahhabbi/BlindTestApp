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

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private boolean isAdmin;
    private boolean gameNameExists;
    private String currentPage = "joinGamePage";



    /********************************************************************/

    /*------------------------------------------------------------
     * Preparing variables to change the scene after joining
     * -----------------------------------------------------------*/
    private Stage stage;
    private Scene scene;
    private static Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void storePlayerInformation(boolean isAdmin, boolean gameNameExists, Socket socket, BufferedReader reader, PrintWriter writer) {
        this.isAdmin = isAdmin;
        this.gameNameExists = gameNameExists;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.start();
    }

    @FXML
    public void goBackHome(ActionEvent event){
        try {
            String title = "BlindTest.IO";
            String pageToLoad = " ";

            if(event.getSource().equals(goBackHome))
            {
                pageToLoad = "/multiplayer/client/mainMultiplayerPage.fxml";
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
            System.out.println("on rentre dans le thread wait for server responses ....");
            String msg = reader.readLine(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)

            while(socket.isConnected()==true && msg!=null &&  currentPage.equals("joinGamePage") == true && gameNameExists==false) {

                handleServerResponse(msg);
                System.out.println("JoinGame SERVER sent : " + msg);

                msg = reader.readLine();   // prochaine message recu
            }
            System.out.println("FIN THREAD JOIN GAME CONTROLLER ");

        } catch (Exception e) {
            e.printStackTrace();
            Controller.closeEverything(socket, reader, writer);
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
    public void submitGameName(ActionEvent e) {
        Window windowOwner = ((Node)e.getSource()).getScene().getWindow();
        String gameName = gameNameFieldJ.getText().toLowerCase();

        if (gameName.isEmpty() == true){
            Controller.displayAlert(Alert.AlertType.ERROR,windowOwner,"GameName vide", "Veuillez saisir un nom de partie");
            return;
        }

        /* player chose "create a game"*/
        if(isAdmin == true){

            System.out.println("->  IS ADMIN");
            writer.println("create " + gameName);  // envoi le pseudo au ClientHandler
            System.out.println("submitGameName() -> Sent \"create "+ gameName +"\" to Client handler");

            try {
                Thread.sleep(1000);
                System.out.println("GAME EXISTS = "+ gameNameExists);

                if(this.gameNameExists == true){
                    Controller.displayAlert(Alert.AlertType.ERROR,windowOwner,"GameName exits", "Veuillez saisir un nom de partie qui n'existe pas");
                }
                else if (this.gameNameExists == false){
                    changeWindow(e,"/multiplayer/client/multiplayerGamePage.fxml", "BlindTest.IO | Multiplayer game as admin", this.isAdmin, this.gameNameExists,this.socket, this.reader, this.writer);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        /* player chose "join a game" */
        else if (isAdmin == false){
            System.out.println("-> IS NOOOT ADMIN");

            writer.println("join " + gameNameExists);  // envoi le pseudo au ClientHandler
            System.out.println("submitGameName()-> Sent \"join "+ gameName +"\"to Client handler");
            try {
                Thread.sleep(300);
                if(gameNameExists == false){
                    Controller.displayAlert(Alert.AlertType.ERROR,windowOwner,"GameName NOT FOUND", "Veuillez saisir un nom de partie qui existe déjà");
                }
                else{
                    changeWindow(e,"/multiplayer/client/multiplayerGamePage.fxml", "BlindTest.IO | Multiplayer game as simple player", this.isAdmin, this.gameNameExists,this.socket, this.reader, this.writer);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void changeWindow(ActionEvent e, String pageToLoad, String title, boolean isAdmin, boolean gameNameExists, Socket socket, BufferedReader reader, PrintWriter writer){
        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(pageToLoad)));
            root = loader.load();


            /* create a page JoinGameController */
            PlayerController playerController = loader.getController();
            System.out.println("sending player info to playerController");
            playerController.storePlayerInformation(isAdmin, gameNameExists, socket, reader, writer);

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
