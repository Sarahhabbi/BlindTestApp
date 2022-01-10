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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import multiplayer.network.ComSocket;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller extends Thread implements Initializable {

    /* for mainMultiplayerPage : */
    @FXML
    private Button quitBtn;

    @FXML
    private Button submitPseudo;

    @FXML
    private TextField usernameField;

    @FXML
    private AnchorPane globalContainer;

    @FXML
    private Text usernameText;

    /********************************************************************/


    private ComSocket comSocket;

    private boolean isAdmin;

    private boolean pseudoExists;
    private String playerPseudo;
    private boolean gameNameExists;


    private String currentPage = "mainMultiplayerPage";

    /********************************************************************/

    private Stage stage;
    private Scene scene;
    private static Parent root;

    /********************************************************************/


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(comSocket == null){
            System.out.println("CONTROLLER");
            connectSocket();
        }
    }
    @FXML
    public void goBackHome(ActionEvent event) throws IOException {
        try {
            System.out.println("Closing everything, player is going back home");

            String title = "BlindTest.IO";
            String pageToLoad = " ";

            if(event.getSource().equals(quitBtn))
            {
                pageToLoad = "/application/startPage.fxml";
            }

            this.comSocket.closeEverything();

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
    public void quitGame (ActionEvent event){
        System.out.println("Closing everything, player is quiting");
        this.comSocket.closeEverything();
        Platform.exit();
    }

    public void connectSocket() {
        try {
            System.out.println("Connection à la socket");
            Socket s = new Socket("192.168.1.42", 2010);
            if(s==null){
                System.out.println("La socket est nulle");
            }
            this.comSocket = new ComSocket(s);
            System.out.println("Socket is connected with server on port 8080!");
            this.start();
        } catch(ConnectException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            String msg = comSocket.read(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)
            while(comSocket.isConnected()==true && msg != null &&  this.currentPage.equals("mainMultiplayerPage")==true && pseudoExists == true){

                System.out.println(currentPage + " SERVER sent : " + msg);

                String[] words = msg.split(" ");
                if (words[0].equals("/") == true) {
                    handleServerResponse(msg);
                }
                msg = comSocket.read();
            }
                System.out.println("FIN THREAD CONTROLLER");
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            e.printStackTrace();
            this.comSocket.closeEverything();
        }
    }
    /****************************************  PSEUDO  *****************************************************/

    public void handleServerResponse(String msg) {
        switch(msg){
            case "/ EXISTS_PSEUDO":
                /* TO DISPLAY ERROR FOR PSEUDO */
                this.pseudoExists = true;
                System.out.println(" DEBUG / EXISTS_PSEUDO "+ pseudoExists);
                break;

            case "/ UNIQUE_PSEUDO":
                /* MAJ UI */
                this.pseudoExists = false;
                System.out.println(" DEBUG / UNIQUE_PSEUDO "+ pseudoExists);
                break;
            default:
                System.out.println("any case");
                break;
        }
    }



    // sending pseudo to ClientHandler
    public void submitPseudo(ActionEvent e){
        Window windowOwner = ((Node)e.getSource()).getScene().getWindow();
        String pseudo = usernameField.getText().toLowerCase();
        if (pseudo.isEmpty() == true){
            displayAlert(Alert.AlertType.ERROR,windowOwner,"Pseudo vide", "Veuillez saisir un pseudo");
        }
        else{
            try{
                sendPseudo(pseudo);
                Thread.sleep(300);

                /* MAJ UI en fonction du message d'erreur*/
                if(this.pseudoExists == true){
                    System.out.println(" submitPseudo "+ pseudoExists);
                    displayAlert(Alert.AlertType.ERROR, windowOwner, "Pseudo pris", "Pseudo déjà utilisé veuillez essayer à nouveau");
                }

                else if (this.pseudoExists == false){
                    this.playerPseudo = pseudo;
                    System.out.println("rejoignez ou créez une partie !");
                    submitPseudo.setDisable(true);
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    public void sendPseudo(String pseudo) throws IOException {
        comSocket.write(pseudo);  // envoi le pseudo au ClientHandler
        System.out.println("sendPseudo() -> Sent pseudo ("+ pseudo +") to Client handler");
    }

    /****************************************  GAME  *****************************************************/

    public void createGameBtn(ActionEvent e) {
        Window windowOwner = ((Node)e.getSource()).getScene().getWindow();

        String pseudo = usernameField.getText().toLowerCase();
        if(pseudo.isEmpty() == true || playerPseudo==null || pseudoExists==true){
            displayAlert(Alert.AlertType.ERROR, windowOwner, "Entrez un pseudo", "Veuillez entrer un pseudo");
        }else{
            this.currentPage = "createGamePage";
            changeWindow(e,"/multiplayer/client/createGamePage.fxml", "BlindTest.IO | Join game", true, this.gameNameExists,comSocket);
        }
    }

    public void joinGameBtn(ActionEvent e) {
        Window windowOwner = ((Node)e.getSource()).getScene().getWindow();

        String pseudo = usernameField.getText().toLowerCase();
        if(pseudo.isEmpty() == true || playerPseudo==null || pseudoExists==true){
            displayAlert(Alert.AlertType.ERROR, windowOwner, "Entrez un pseudo", "Veuillez entrer un pseudo");
        }else{
            changeWindow(e,"/multiplayer/client/joinGamePage.fxml", "BlindTest.IO | Join game", false, this.gameNameExists,comSocket);
        }
    }

    //rediriger vers une autre page
    public void changeWindow(ActionEvent e, String pageToLoad, String title, boolean isAdmin, boolean gameNameExists,ComSocket comSocket){
        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(pageToLoad)));
            root = loader.load();

            if(pageToLoad.equals("/multiplayer/client/joinGamePage.fxml")){
                this.currentPage = "joinGamePage";
                JoinGameController joinGameController = loader.getController();
                System.out.println("sending player info to JOIN GAME CONTROLLER");
                joinGameController.storePlayerInformation(isAdmin, gameNameExists,comSocket);
            }

            else{
                this.currentPage = "createGamePage";

                CreateGameController createGameController = loader.getController();
                System.out.println("sending player info to CREATE GAME CONTROLLER");
                createGameController.storePlayerInformation(isAdmin, gameNameExists,comSocket);
            }

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
    //afficher des pop up pour les erreurs ou les succes
    public static void displayAlert(Alert.AlertType alertType, Window windowOwner, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(windowOwner);
        alert.show();
    }





    public static void closeEverything(ComSocket comSocket) {
        comSocket.closeEverything();

    }

}
