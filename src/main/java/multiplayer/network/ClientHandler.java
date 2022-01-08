package multiplayer.network;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientHandler extends Thread {

    public static Map<String,ClientHandler> clientHandlers = Collections.synchronizedMap(new HashMap<String,ClientHandler>());

    private Socket socket;  //socket du front
    private BufferedReader bufferedReader; //read message
    private PrintWriter writer;

    private String playerPseudo;
    private String joinedGame;       // le joueur enverra le nom de la partie qu'il rejoins
    private GameHandler gameHandler;
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            Future<Integer> future = initPseudo();

            while(!future.isDone()){
            }

            this.clientHandlers.put(playerPseudo,this);
            this.joinedGame = null;
            System.out.println("SERVER: " + playerPseudo + " is connected to the server !");
            this.start();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, writer);
            e.printStackTrace();
        }
    }

    public Future<Integer> initPseudo() {
        return executor.submit(() -> {
            // verifier que pseudo est unique sinon envoyer une exception au Client
            String pseudo = null;
            try {
                pseudo = bufferedReader.readLine();
                boolean pseudoIsNotUnique = clientHandlers.containsKey(pseudo); // false si clientHandler n'a pas ce pseudo
                int tries = 0;

                while (pseudoIsNotUnique == true && tries < 4) {
                    writer.println("/ exists");

                    pseudo = bufferedReader.readLine();
                    pseudoIsNotUnique = clientHandlers.containsKey(pseudo);
                    tries++;
                }
                writer.println("/ unique");

                playerPseudo = pseudo;  // username is sent in sendPseudo() method in Client class
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    /* listen to player answers*/
    @Override
    public void run() {
        try {
            String answer = bufferedReader.readLine();

            while (socket.isConnected()==true && answer!=null) {
                System.out.println("CLIENT SENT : "+ answer);
                handlePlayerAnswer(answer);
                answer = bufferedReader.readLine();
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
                String arg = parseAnswer[1];
                switch(first) {
                    case "create":
                        createGame(arg);
                        break;
                    case "join":
                        joinGame(arg);
                        break;
                    case "start":
                        startGame();
                        break;
                    default :
//                        gameHandler.giveAnswer();
                        break;
                }
            }
        }
    }

    public void createGame(String gameName){
        GameHandler game = null;
        try {
            game = GameHandler.addGame(gameName, this.playerPseudo);
            setGameHandler(game);
            game.addPlayer(this);
            writer.println("unique");
            System.out.println(this.playerPseudo + " created " + gameName);
        }
        catch (Exception e) {
            writer.println(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void joinGame(String gameName){
        GameHandler game = null;
        try {
            game = GameHandler.getGame(gameName);
            setGameHandler(game);
            game.addPlayer(this);
            System.out.println(this.playerPseudo + " joined " + gameName + " game");
            writer.println("joined");
        }
        catch (Exception e) {
            writer.println(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void startGame(){
        if(gameHandler.getAdmin().equals(this.playerPseudo) == true){
            gameHandler.start();
        }else{
            System.out.println("not admin cannot start the game");
            writer.println("not admin cannot start the game"); // normalemet il n'y aura jamais ce cas
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
