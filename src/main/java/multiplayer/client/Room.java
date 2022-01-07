package multiplayer.client;

import javafx.fxml.Initializable;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Room extends Thread implements Initializable {

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 8889);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
            }

        } catch (Exception e) {
            closeEverything(socket, reader, writer);
            e.printStackTrace();
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, PrintWriter writer) {
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(writer != null){
                writer.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleSendEvent(ActionListener event) {
        send();
    }

    public void send() {
        /*String msg = msgField.getText();
        writer.println(Controller.username + ": " + msg);

        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }*/
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectSocket();
    }
}
