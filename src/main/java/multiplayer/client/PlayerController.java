package multiplayer.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.MyImage;
import models.PlayerAnswer;
import multiplayer.network.ComSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerController  extends Thread implements Initializable {

    @FXML
    private TextField answerField;

    @FXML
    private Button submitBtn;

    @FXML
    private ImageView image;
    /********************************************************************/

    private ComSocket comSocket;
    private boolean isAdmin;
    private boolean pseudoExists;
    private boolean gameNameExists;
    private String currentPage = "playerPage";
    private String gameName;

    /********************************************************************/


    @Override
    public void run(){
        try {
            System.out.println("On entre dans PLAYER THREAD");
            String msg = null;
           while(msg==null){
                try{
                    msg = comSocket.read(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)
                    System.out.println("1 PlayerController SERVER sent : " + msg);

                }catch (Exception e) {
                    System.out.println("C'est complique");
                    msg=null;
                }
            }
            System.out.println(msg);
            this.jouer();
            //System.out.println("FIN THREAD ADMIN CONTROLLER ");

        } catch (Exception e) {
            e.printStackTrace();
            Controller.closeEverything(comSocket);
        }
    }
    public void jouer() {

            while (comSocket.isConnected() == true) {
                try{
                    String msg = comSocket.read();
                    switch(msg) {
                        case "/  GAME START":
                        case "/ START IN 5s":
                            String image= comSocket.read();
                            String[] words =image.split(" ");
                            MyImage i=new MyImage(words[2],words[3]);
                            System.out.println(i.getId()+" "+i.getAnswer());
                        case "/ SEND ME ANSWER":
                            PlayerAnswer p = new PlayerAnswer("image",1000,"reponse");
                            comSocket.write("image 140000 reponse");

                            break;
                        case "/ FIN DU TOUR":

                        case "/ FIN DU JEU":
                            break;
                        default :

                    }
                }catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }




    @FXML
    public void submitAnswer(ActionEvent e){
        String answer = answerField.getText().toLowerCase();
        /* creer object Answer*/
        /* envoyer au back en écrivant dans la socket */

    }

    public void changeImage(MyImage newImage){
        Image i =new Image(image.getId());
        // change the image
        image.setImage(i);
        System.out.println("new good answer = "+ newImage.getAnswer());
    }
    public void storePlayerInformation(boolean isAdmin, boolean gameNameExists, ComSocket comSocket, String gameName) {
        this.isAdmin = isAdmin;
        this.gameNameExists = gameNameExists;
        this.comSocket=comSocket;
        this.gameName = gameName;
        System.out.println("RECEIVED gameName = " + this.gameName);
        this.start();
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
