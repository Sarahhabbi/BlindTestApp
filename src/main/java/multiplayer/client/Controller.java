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
            while(socket.isConnected() == true) {
                String msg = reader.readLine(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (GameHandler?
                System.out.println("SERVER sent : " + msg);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            closeEverything(socket, reader, writer);
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
        Window windowOwner = submitPseudo.getScene().getWindow();

        writer.println(pseudo);  // envoi le pseudo au ClientHandler
        System.out.println("sendPseudo() -> Sent pseudo ("+ pseudo +") to Client handler");
        try {
            String msg = reader.readLine();
            if(msg.equals("Pseudo already exists")){
                displayAlert(Alert.AlertType.ERROR, windowOwner, "Pseudo Pris", "Pseudo déjà utilisé");
            }
            else{

                //enlever les éléments inutiles
                globalContainer.getChildren().remove(usernameField);
                globalContainer.getChildren().remove(submitPseudo);
                globalContainer.getChildren().remove(usernameText);

                //créer les boutons à ajouter
                Button createGameBtn = new Button("Create Game");
                Button joinGameBtn = new Button("Join Game");

                //styliser les boutons
                createGameBtn.setStyle("-fx-background-radius: 15px" +
                        "-fx-font-family: Verdana Pro Cond Semibold"
                );
                joinGameBtn.setStyle("-fx-background-radius: 15px" +
                        "-fx-font-family: Verdana Pro Cond Semibold"
                );

                createGameBtn.setPadding(new Insets(5,5,5,5));



                //ajouter les boutons dans le container principal
                globalContainer.getChildren().add(createGameBtn);
                globalContainer.getChildren().add(joinGameBtn);



            }
        } catch (IOException e) {
            e.printStackTrace();
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
