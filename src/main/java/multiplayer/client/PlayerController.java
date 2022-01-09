package multiplayer.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private boolean isAdmin;
    private boolean pseudoExists;
    private boolean gameNameExists;
    private String currentPage = "playerPage";
    /********************************************************************/



    @Override
    public void run(){
        try {
            String msg = reader.readLine(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)

            while(socket.isConnected()==true && msg!=null) {

                handleServerResponse(msg);
                System.out.println("PlayerController SERVER sent : " + msg);

                msg = reader.readLine();   // prochaine message recu
            }
            System.out.println("FIN THREAD PLAYER GAME CONTROLLER ");

        } catch (Exception e) {
            e.printStackTrace();
            Controller.closeEverything(socket, reader, writer);
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

    public void storePlayerInformation(boolean isAdmin, boolean gameNameExists, Socket socket, BufferedReader reader, PrintWriter writer) {
        this.isAdmin = isAdmin;
        this.gameNameExists = gameNameExists;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
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
