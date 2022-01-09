package multiplayer.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import multiplayer.network.ComSocket;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController extends Thread implements Initializable  {

    @FXML
    private javafx.scene.control.TextField answerField;

    @FXML
    private Button submitBtn;

    @FXML
    private Button startBtn;

    /********************************************************************/

    private ComSocket comSocket;
    private boolean isAdmin;
    private boolean pseudoExists;
    private boolean gameNameExists;
    private String gameName;
    private String currentPage = "adminPage";
    private boolean started=false;

    /********************************************************************/

    public void storePlayerInformation(boolean isAdmin, boolean gameNameExists, ComSocket comSocket, String gameName) {
        this.isAdmin = isAdmin;
        this.gameNameExists = gameNameExists;
        this.comSocket=comSocket;
        this.gameName = gameName;
        System.out.println("RECEIVED gameName = " + this.gameName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void run(){
        try {
            String msg = comSocket.read();
            while(comSocket.isConnected()==true && msg!=null) {

                System.out.println("Admin Controller SERVER sent : " + msg);

                msg = comSocket.read();   // prochaine message recu
            }
            System.out.println("FIN THREAD AMIN CONTROLLER ");

        } catch (Exception e) {
            e.printStackTrace();
            Controller.closeEverything(comSocket);
        }
    }

    private void handleServerResponse(String msg) {
    }

    public void startGame(ActionEvent e) throws IOException {
        // envoyer au client handler un message "start gameName"
        if (started == false){
            System.out.println("START");
            comSocket.write("start " + gameName);
            started=true;
            this.start();
        }
    }


}
