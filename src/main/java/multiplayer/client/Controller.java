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
import javafx.scene.layout.StackPane;
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
            closeEverything(socket, reader, writer);

            String title = "BlindTest.IO";
            String pageToLoad = " ";

            if(event.getSource().equals(quitBtn))
            {
                pageToLoad = "/application/startPage.fxml";
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
            String msg = reader.readLine(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (GameHandler?
            System.out.println("SERVER sent : " + msg);
            String [] words = msg.split(" ");

            if(words[0].equals("/") == true){
//                handleServerResponse(msg);

            }
            if(msg.equals("Pseudo already exists")) {
                Window windowOwner = submitPseudo.getScene().getWindow();
                displayAlert(Alert.AlertType.ERROR, windowOwner, "Pseudo pris", "Pseudo déjà utilisé veuillez essayer à nouveau");
            }
            else if(msg.equals("Pseudo is unique")) {
                System.out.println("4 DEBUG REMOVING ELMENTS ");

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            closeEverything(socket, reader, writer);
        }
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, PrintWriter writer) {
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
        String pseudo = usernameField.getText().toLowerCase();
        try{
            sendPseudo(pseudo);
        }catch(Exception e){

        }
    }
    public void sendPseudo(String pseudo) {
        writer.println(pseudo);  // envoi le pseudo au ClientHandler

        System.out.println("sendPseudo() -> Sent pseudo ("+ pseudo +") to Client handler");
    }


    public void update(){

        System.out.println("ICI ON ARRIVE");
        //enlever les éléments inutiles
        globalContainer.getChildren().remove(usernameField);
        globalContainer.getChildren().remove(submitPseudo);
        globalContainer.getChildren().remove(usernameText);

        //créer les boutons à ajouter
        Button createGameBtn = new Button("Create Game");
        Button joinGameBtn = new Button("Join Game");
        StackPane centerPane = new StackPane();
        System.out.println(" 1 DEBUG REMOVING ELMENTS ");
        System.out.println(" 2 DEBUG REMOVING ELMENTS ");
        centerPane.setMinSize(200, 200);
        centerPane.setLayoutX(117);
        centerPane.setLayoutY(200);

        //************************
        globalContainer.getChildren().add(centerPane);
        System.out.println(" 3 DEBUG REMOVING ELEMENTS ");
        //styliser les boutons
        createGameBtn.setStyle("-fx-background-radius: 15px");
        createGameBtn.setStyle("-fx-font-family: Verdana Pro Cond Semibold");


        joinGameBtn.setStyle("-fx-background-radius: 15px");
        joinGameBtn.setStyle("-fx-font-family: Verdana Pro Cond Semibold");

        createGameBtn.setPadding(new Insets(5,5,5,5));
        joinGameBtn.setPadding(new Insets(5,5,5,5));

        createGameBtn.setLayoutX(74);
        createGameBtn.setLayoutY(48);

        joinGameBtn.setLayoutX(74);
        joinGameBtn.setLayoutY(130);

        centerPane.getChildren().add(createGameBtn);
        centerPane.getChildren().add(joinGameBtn);
    }

    public void changeWindow() {
        /*try {
            Stage stage = (Stage) userName.getScene().getWindow();
            Parent root = FXMLLoader.load(this.getClass().getResource("Room.fxml"));
            stage.setScene(new Scene(root, 330, 560));
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    //afficher des pop up pour les erreurs ou les succes
    private static void displayAlert(Alert.AlertType alertType, Window windowOwner, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(windowOwner);
        alert.show();
    }

}
