package multiplayer.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.MyImage;
import models.PlayerAnswer;
import multiplayer.network.ComSocket;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController extends Thread implements Initializable {

    @FXML
    private javafx.scene.control.TextField answerField;

    @FXML
    private Button submitBtn;

    @FXML
    private Button startBtn;

    @FXML
    private ImageView image;
    /********************************************************************/

    private ComSocket comSocket;
    private boolean isAdmin;
    private boolean pseudoExists;
    private boolean gameNameExists;
    private String gameName;
    private String currentPage = "adminPage";
    private boolean started = false;

    /********************************************************************/

    public void storePlayerInformation(boolean isAdmin, boolean gameNameExists, ComSocket comSocket, String gameName) {
        this.isAdmin = isAdmin;
        this.gameNameExists = gameNameExists;
        this.comSocket = comSocket;
        this.gameName = gameName;
        System.out.println("RECEIVED gameName = " + this.gameName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void run() {
        try {
            System.out.println("On entre dans admin THREAD");
            String msg = null;
            while(msg==null){
                try{
                    msg = comSocket.read(); // LIT LES MESSAGES QUE LE SERVER LUI A ENVOYE (ClientHandler et GameHandler)
                    System.out.println("1 PlayerController SERVER sent : " + msg);

                }catch (Exception e) {
                    System.out.printf("C'est complique");
                    msg=null;
                }
            }
            while (comSocket.isConnected() == true && msg != null) {
                if (msg.equals("/ GAME START") == true) {
                    System.out.println("Admin Controller SERVER sent : " + msg);
                    this.jouer();
                }
            }
            System.out.println("FIN THREAD ADMIN CONTROLLER ");

        } catch (Exception e) {
            e.printStackTrace();
            Controller.closeEverything(comSocket);
        }
    }


    public void jouer() {

        try{
            while (comSocket.isConnected() == true) {
                String msg = comSocket.read();
                switch(msg) {
                    case "/ START IN 5s":
                        String image= comSocket.read();
                        String[] words =image.split(" ");
                        MyImage i=new MyImage(words[2],words[3]);
                        System.out.println(i.getId()+" "+i.getAnswer());
                    case "/ SEND ME ANSWER":
                        PlayerAnswer p = new PlayerAnswer("iamge",1000,"reponse");
                        comSocket.write("image 140000 reponse");

                        break;
                    case "/ FIN DU TOUR":

                    case "/ FIN DU JEU":
                        break;
                    default :

                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }






    /*if(msg.equals("/ START IN 5s")==true)

    {

        try {
            MyImage image = comSocket.readMyImage();
            changeImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }else

    {
        msg = comSocket.read();   // prochaine message recu
        handleServerResponse(msg);
    }

}*/
    private void handleServerResponse(String msg) {
        switch(msg){

            /* for create game */
            case "/ START IN 5s":
                System.out.println("DEBUG / START IN 5s");
                break;

            case "/ SEND ME ANSWER":
                System.out.println("DEBUG / SEND ME ANSWER");
                break;

            case "/ FIN DU TOUR":
                System.out.println("DEBUG / FIN DU TOUR");
                break;

            default:
                System.out.println("any case player game controller");
                break;
        }
    }
    public void changeImage(MyImage newImage){
        javafx.scene.image.Image i =new Image(image.getId());
        // change the image
        image.setImage(i);
        System.out.println("new good answer = "+ newImage.getAnswer());
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
