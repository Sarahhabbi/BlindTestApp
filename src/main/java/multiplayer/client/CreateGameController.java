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
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreateGameController extends Thread implements Initializable {
    @FXML
    private TextField gameNameFieldJ;

    @FXML
    private Button goBackHome;

    /********************************************************************/

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private boolean isAdmin;
    private boolean pseudoExists;
    private boolean gameNameExists;
    private String currentPage = "createGamePage";

    /********************************************************************/

    /*------------------------------------------------------------
     * Preparing variables to change the scene after joining
     * -----------------------------------------------------------*/
    private Stage stage;
    private Scene scene;
    private static Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("HELLO FROM CREATE GAME CONTROLLER");
//        System.out.println("Player info :" );
//        System.out.println("---> is admin ? " + this.isAdmin);
//        System.out.println("---> game exists ?" + this.gameNameExists);
//        System.out.println("---> current page ? " + this.currentPage);
//        System.out.println("---> socket" + socket);
//        System.out.println("---> reader" + reader);
//        System.out.println("---> writer" + writer);
    }

    @FXML
    public void storePlayerInformation(boolean isAdmin, boolean gameNameExists, Socket socket, BufferedReader reader, PrintWriter writer) {
        this.isAdmin = true;
        this.gameNameExists = gameNameExists;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.start();
    }

    @Override
    public void run(){
        try {
            System.out.println("on rentre dans le thread Create game controller wait for server responses ....");
            String msg = this.reader.readLine(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)

            while(socket.isConnected()==true && msg!=null &&  currentPage.equals("createGamePage") && gameNameExists==true) {

                handleServerResponse(msg);

                System.out.println("createGamePage SERVER sent : " + msg);
                msg = this.reader.readLine();   // prochaine message recu
            }
            System.out.println("FIN THREAD CREATE GAME CONTROLLER ");

        } catch (Exception e) {
            e.printStackTrace();
            Controller.closeEverything(socket, reader, writer);
        }
    }

    public void handleServerResponse(String msg) {
        switch(msg){

            /* for create game */
            case "/ UNIQUE_GAMENAME":
                System.out.println("DEBUG / UNIQUE_GAMENAME");
                this.gameNameExists = false;
                System.out.println("game exists " + gameNameExists);
                break;

            case "/ EXISTS_GAMENAME":
                System.out.println("DEBUG / EXISTS_GAMENAME");
                this.gameNameExists = true;
                System.out.println("game exists " + gameNameExists);
                break;

            default:
                System.out.println("any case create game");
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


            AdminController adminController = loader.getController();
            System.out.println("sending player info to adminController");
            adminController.storePlayerInformation(isAdmin, gameNameExists, socket, reader, writer);

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

    public void quitFromGameCreate(ActionEvent actionEvent) {
        Platform.exit();
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
}
