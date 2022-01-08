package multiplayer.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.*;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller extends Thread implements Initializable {


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


    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private boolean isAdmin;
    private String currentPage = "mainMultiplayerPage";

    /********************************************************************/

    private Stage stage;
    private Scene scene;
    private static Parent root;

    /********************************************************************/


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectSocket();
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

            closeEverything(socket, reader, writer);

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
        closeEverything(socket, reader, writer);
        Platform.exit();
    }

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 1234);
            System.out.println("Socket is connected with server on port 1234!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            String msg = reader.readLine(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)
            while(socket.isConnected()==true && msg != null && currentPage.equals("mainMultiplayerPage")) {

                System.out.println("Controllerrrr SERVER sent : " + msg);

                String[] words = msg.split(" ");
                if (words[0].equals("/") == true) {
                    handleServerResponse(msg);
                }
                msg = reader.readLine();   // prochaine message recu
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            e.printStackTrace();
            closeEverything(socket, reader, writer);
        }
    }

    public void handleServerResponse(String msg) {
        switch(msg){
            case "/ exists":
                /* DISPLAY ERROR FOR PSEUDO */
                Window windowOwner = submitPseudo.getScene().getWindow();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        displayAlert(Alert.AlertType.ERROR, windowOwner, "Pseudo pris", "Pseudo déjà utilisé veuillez essayer à nouveau");
                    }
                });
                break;

            case "/ unique":
                /* MAJ UI */
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
                break;

            default:
                System.out.println("any case");
                break;
        }
    }

    public static void closeEverything(Socket socket, BufferedReader bufferedReader, PrintWriter writer) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // sending pseudo to ClientHandler
    public void submitPseudo(ActionEvent event){
        Window windowOwner = submitPseudo.getScene().getWindow();
        String pseudo = usernameField.getText().toLowerCase();
        if (pseudo.isEmpty() == true){
            displayAlert(Alert.AlertType.ERROR,windowOwner,"Pseudo vide", "Veuillez saisir un pseudo");
        }
        else{
            try{
                sendPseudo(pseudo);
            }catch(Exception e){

            }
        }
    }

    public void sendPseudo(String pseudo) {
        writer.println(pseudo);  // envoi le pseudo au ClientHandler
        System.out.println("sendPseudo() -> Sent pseudo ("+ pseudo +") to Client handler");
    }

    public void update(){

        System.out.println("ICI ON ARRIVE");

        globalContainer.getChildren().remove(usernameField);
        globalContainer.getChildren().remove(submitPseudo);
        globalContainer.getChildren().remove(usernameText);


        Button createGameBtn = new Button("Create Game");
        Button joinGameBtn = new Button("Join Game");
        Pane centerPane = new Pane();

        //centerPane.setStyle("-fx-background-color: red");

        //******************************************************

        centerPane.setPrefSize(200, 200);
        centerPane.setLayoutX(190);
        centerPane.setLayoutY(140);

        globalContainer.getChildren().add(centerPane);

        //***************************************************************


        createGameBtn.setStyle("-fx-background-radius: 15px; " +
                "-fx-font-family: Lucida; " +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 16px");

        createGameBtn.setPrefWidth(110);
        createGameBtn.setPrefHeight(30);


        joinGameBtn.setStyle("-fx-background-radius: 15px; " +
                "-fx-font-family: Lucida; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 16px");

        joinGameBtn.setPrefWidth(110);
        joinGameBtn.setPrefHeight(30);

        //***********************************************************************

        createGameBtn.setPadding(new Insets(5,5,5,5));
        joinGameBtn.setPadding(new Insets(5,5,5,5));

        //**************************************************************************
        createGameBtn.setLayoutX(70);
        createGameBtn.setLayoutY(48);

        joinGameBtn.setLayoutX(70);
        joinGameBtn.setLayoutY(120);

        //*****************************************************************

        centerPane.getChildren().add(createGameBtn);
        centerPane.getChildren().add(joinGameBtn);

        //**********************************************

        createGameBtn.setOnAction(e -> {
            goToJoinGame(e,true);
        });

        joinGameBtn.setOnAction(e -> {
            goToJoinGame(e,false);
        });

    }

    private void goToJoinGame(ActionEvent e, boolean isAdmin) {
        try {
            System.out.println("Joining a game");
            String title = "BlindTest.IO";
            String pageToLoad = "/multiplayer/client/joinGamePage.fxml";
            this.isAdmin = isAdmin;

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(pageToLoad)));
            root = loader.load();

            System.out.println("trying to interrupt current thread");
            this.interrupt();

            currentPage = "joinPage";

            /* create a page JoinGameController */
            JoinGameController joinGameController = loader.getController();
            System.out.println("sending player info to joinGameController");
            joinGameController.storePlayerInformation(socket, reader, writer, isAdmin);

            /* changing the scene */
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            System.out.println("fin debug");
        } catch (Exception exception) {
            exception.printStackTrace();
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

}
