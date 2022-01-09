package multiplayer.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import multiplayer.network.ComSocket;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerController  extends Thread implements Initializable {

    @FXML
    private TextField answerField;

    @FXML
    private Button submitBtn;

    /********************************************************************/

    private ComSocket comSocket;
    private boolean isAdmin;
    private boolean pseudoExists;
    private boolean gameNameExists;
    private String currentPage = "playerPage";

    /********************************************************************/



    @Override
    public void run(){
        try {
            String msg = comSocket.read(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)

            while(comSocket.isConnected()==true && msg!=null) {

                handleServerResponse(msg);
                System.out.println("PlayerController SERVER sent : " + msg);

                msg = comSocket.read();   // prochaine message recu
            }
            System.out.println("FIN THREAD PLAYER GAME CONTROLLER ");

        } catch (Exception e) {
            e.printStackTrace();
            Controller.closeEverything(comSocket);
        }
    }

    private void handleServerResponse(String msg) {

    }

    @FXML
    public void submitAnswer(ActionEvent e){
        String answer = answerField.getText().toLowerCase();
        /* creer object Answer*/
        /* envoyer au back en écrivant dans la socket */

    }

    public void storePlayerInformation(boolean isAdmin, boolean gameNameExists, ComSocket comSocket) {
        this.isAdmin = isAdmin;
        this.gameNameExists = gameNameExists;
        this.comSocket=comSocket;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("HELLO FROM Player GAME PAGE");
//        System.out.println("Player info :" );
//        System.out.println("---> is admin ? " + this.isAdmin);
//        System.out.println("---> game exists ?" + this.gameNameExists);
//        System.out.println("---> current page ? " + this.currentPage);
    }
}
