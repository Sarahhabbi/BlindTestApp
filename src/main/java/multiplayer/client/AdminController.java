package multiplayer.client;

import javafx.fxml.Initializable;
import multiplayer.network.ComSocket;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController extends Thread implements Initializable  {
    private ComSocket comSocket;
    private boolean isAdmin;
    private boolean pseudoExists;
    private boolean gameNameExists;
    private String currentPage = "adminPage";


    public void storePlayerInformation(boolean isAdmin, boolean gameNameExists, ComSocket comSocket) {
        this.isAdmin = isAdmin;
        this.gameNameExists = gameNameExists;
        this.comSocket=comSocket;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("HELLO FROM ADMIN GAME PAGE");
        System.out.println("Player info :" );
        System.out.println("---> is admin ? " + this.isAdmin);
        System.out.println("---> game exists ?" + this.gameNameExists);
        System.out.println("---> current page ? " + this.currentPage);
    }

}
