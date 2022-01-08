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
    private Button submitGameNameBtn;

    @FXML
    private Button goBackHome;

    @FXML
    private Button quitFromGameJoin;

    /********************************************************************/

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private boolean isAdmin;
    private String gameName;
    private String currentPage = "joinGame";


    /********************************************************************/

    /*------------------------------------------------------------
     * Preparing variables to change the scene after joining
     * -----------------------------------------------------------*/
    private Stage stage;
    private Scene scene;
    private static Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.start();
    }

    @FXML
    public void goBackHome(ActionEvent event) throws IOException {
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
            System.out.println("on rentre dans le thread wait for server responses ....");
            String msg = reader.readLine(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)

            System.out.println("JoinGameeeee 1 SERVER sent : " + msg);

            while(socket.isConnected()==true && msg!=null &&  currentPage.equals("joinGame")) {

                System.out.println("JoinGameeeee 2 SERVER sent : " + msg);
                handleServerResponse(msg);

                System.out.println("JoinGameeeee 3 SERVER sent : " + msg);
                msg = reader.readLine();   // prochaine message recu
                handleServerResponse(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e.getMessage());
            Controller.closeEverything(socket, reader, writer);
        }
    }

    public void handleServerResponse(String msg) {
        switch(msg){
//            case "exists" :
//                /*Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        Window windowOwner = submitGameNameBtn.getScene().getWindow();
//                        Controller.displayAlert(Alert.AlertType.ERROR, windowOwner, "Game name exists !", "Ce nom de partie est déjà pris, réessayez");
//                    }
//                });*/
//                break;

           case "unique":
               System.out.println("successfully created " + gameName);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        changeWindow("/multiplayer/client/multiplayerGamePage.fxml", "BlindTest.IO | Mutliplayer game" + gameName);
                    }
                });
                break;

           case "joined" :
               System.out.println("successfully joined " + gameName);
               this.interrupt(); /* interrupt current thread */
               Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        changeWindow("/multiplayer/client/multiplayerGamePage.fxml", "BlindTest.IO | Mutliplayer game" + gameName);
                    }
                });
                break;

//           case "started":
//               System.out.println("The game already started");
//               this.interrupt(); /* interrupt current thread */
//                /*Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        Window windowOwner = submitGameNameBtn.getScene().getWindow();
//                        Controller.displayAlert(Alert.AlertType.ERROR, windowOwner, "Game already started !", "La partie a déjà commencée...");
//                    }
//                });*/
//                break;
           default:
               System.out.println("any case");
               break;
        }
    }

    @FXML
    public void storePlayerInformation(Socket socket, BufferedReader reader, PrintWriter writer, boolean isAdmin){
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.isAdmin = isAdmin;
        System.out.println("RECEIVED PLAYER INFO !");
    }

    /* envoie au client handler "create gameName" ou "join gameName" -> need to handle clientHandler responses*/
    public void submitGameNameBtn(ActionEvent actionEvent) {
        Window windowOwner = submitGameNameBtn.getScene().getWindow();
        String gameName = gameNameFieldJ.getText().toLowerCase();

        if (gameName.isEmpty() == true){
            Controller.displayAlert(Alert.AlertType.ERROR,windowOwner,"Pseudo vide", "Veuillez saisir un pseudo");
            return;
        }

        /* player chose "create a game"*/
        if(isAdmin == true){
            writer.println("create " + gameName);  // envoi le pseudo au ClientHandler
            System.out.println("submitGameNameBtn -> Sent create "+ gameName +" to Client handler");
        }

        /* player chose "join a game" */
        else {
            writer.println("join " + gameName);  // envoi le pseudo au ClientHandler
            System.out.println("submitGameNameBtn -> Sent join "+ gameName +" to Client handler");
        }
        this.gameName = gameName;

    }

    public void changeWindow(String pageToLoad, String title){
        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(pageToLoad)));
            root = loader.load();

            /* create a page JoinGameController */
            MultiplayerGameController multiplayerGameController = loader.getController();
            System.out.println("sending player info to multiplayerGameController");
            multiplayerGameController.storePlayerInformation(socket, reader, writer, isAdmin);

            /* changing the scene */
            stage = (Stage) submitGameNameBtn.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
