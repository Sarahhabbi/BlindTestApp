package multiplayer.network;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientHandler implements Runnable {

    public static List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
    private Socket socket;  //socket du front

    private BufferedReader bufferedReader; //read message
    private PrintWriter writer;
    private String playerPseudo;
    private String joinedGame;       // le joueur enverra le nom de la partie qu'il rejoins
    private GameHandler gameHandler;


    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

            this.playerPseudo = bufferedReader.readLine();  // username is sent in sendMessage() method in Client class
            this.clientHandlers.add(this);
            this.joinedGame = null;

            System.out.println("SERVER: " + playerPseudo + " is connected to the server !");

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, writer);
            e.printStackTrace();
        }
    }

    /* listen to player answers*/
    @Override
    public void run() {
        try {
            String answer = bufferedReader.readLine();

            while (answer != null) {

                handlePlayerAnswer(answer);
                /* if(answer.equals("start") == true){   // admin à envoyer "start"
                    gameHandler.startGame();
                }else{
                    gameHandler.giveAnswer(answer);
                } */
            }
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, writer);
            e.printStackTrace();
        }

    }

    public void handlePlayerAnswer(String answer) {
        if(answer != null){
            String[] parseAnswer = answer.split(" ");
            String first = parseAnswer[0];

            if(parseAnswer.length >= 2){

                switch(first) {
                    case "create":
                    case "join":

                        if(parseAnswer.length >= 2){
                            String name = parseAnswer[1];
                            GameHandler game = null;
                            try {
                                game = GameHandler.getGame(name);
                                setGameHandler(game);
                                game.addPlayer(this);
                            } catch (Exception e) {
                                writer.println(e.getMessage());
                                System.out.println(e.getMessage());
                            }
                        }
                        System.out.println("COULDN'T JOIN NO GAME NAME");

                    case "start":
                        Thread t = new Thread(gameHandler);
                        t.start();
                    default :
//                        gameHandler.giveAnswer();
                        /* player answer to question */

                }
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
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
            removeClientHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }
    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }
    public PrintWriter getWriter() {
        return writer;
    }
    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }
    public String getPlayerPseudo() {
        return playerPseudo;
    }
    public void setPlayerPseudo(String playerPseudo) {
        this.playerPseudo = playerPseudo;
    }
    public String getJoinedGame() {
        return joinedGame;
    }
    public void setJoinedGame(String joinedGame) {
        this.joinedGame = joinedGame;
    }
    public GameHandler getGameHandler() {
        return gameHandler;
    }
    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

}
