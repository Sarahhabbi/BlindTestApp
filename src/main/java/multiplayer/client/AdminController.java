package multiplayer.client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AdminController {
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private boolean isAdmin;
    private boolean pseudoExists;
    private boolean gameNameExists;
    private String currentPage = "adminPage";


}
