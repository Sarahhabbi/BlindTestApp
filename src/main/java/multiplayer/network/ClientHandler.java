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

    private ComSocket comSocket;
    private String playerPseudo;
    private String joinedGame;       // le joueur enverra le nom de la partie qu'il rejoins
    private GameHandler gameHandler;
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    public ClientHandler(Socket socket) {
        try{
            this.comSocket=new ComSocket(socket);
            Future<Integer> future = initPseudo();

            while(!future.isDone()){
            }

            this.clientHandlers.put(playerPseudo,this);
            this.joinedGame = null;
            System.out.println("SERVER: " + playerPseudo + " is connected to the server !");
            this.start();
        } catch (IOException e) {
            closeEverything(comSocket);
            e.printStackTrace();
        }
    }

    public Future<Integer> initPseudo() {
        return executor.submit(() -> {
            // verifier que pseudo est unique sinon envoyer une exception au Client
            int tries=0;
            String pseudo = null;
            try {
                pseudo =comSocket.read();
                boolean pseudoIsNotUnique = clientHandlers.containsKey(pseudo); // false si clientHandler n'a pas ce pseudo
                while (pseudoIsNotUnique == true) {
                    comSocket.write("/ EXISTS_PSEUDO");

                    pseudo = comSocket.read();
                    pseudoIsNotUnique = clientHandlers.containsKey(pseudo);
                    tries++;
                }
                comSocket.write("/ UNIQUE_PSEUDO");

                playerPseudo = pseudo;  // username is sent in sendPseudo() method in Client class
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }/*catch(Exception e){
                e.printStackTrace();
            }*/
        });
    }

    /* listen to player answers*/
    @Override
    public void run() {
        try {
            String answer = comSocket.read();

            while (comSocket.isConnected()==true && answer!=null) {
                System.out.println("CLIENT SENT : "+ answer);
                handlePlayerAnswer(answer);
                answer = comSocket.read();
            }
        } catch (Exception e) {
            closeEverything(comSocket);
            e.printStackTrace();
        }

    }

     public void handlePlayerAnswer(String answer) throws IOException {
        if(answer != null){
            String[] parseAnswer = answer.split(" ");
            String first = parseAnswer[0];

            if(parseAnswer.length >= 2){
                String arg = parseAnswer[1];
                switch(first) {
                    case "create":
                        createGame(arg,false,5);
                        break;
                    case "join":
                        joinGame(arg);
                        break;
                    case "start":
                        /* ICI METTRE CONDITION START A TRUE POUR KILL RUN*/
                        startGame();
                        break;
                    default :
//                        gameHandler.giveAnswer();
                        break;
                }
            }
        }
    }

    public void createGame(String gameName,boolean isaudio,int round) throws IOException {
        GameHandler game = null;
        try {
            game = GameHandler.addGame(gameName, this.playerPseudo,isaudio,round);
            setGameHandler(game);
            game.addPlayer(this);
            comSocket.write("/ UNIQUE_GAMENAME");
            System.out.println(this.playerPseudo + " created " + gameName);
        }
        catch (Exception e) {
            comSocket.write(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void joinGame(String gameName) throws IOException {
        GameHandler game = null;
        try {
            game = GameHandler.getGame(gameName);
            setGameHandler(game);
            game.addPlayer(this);
            System.out.println(this.playerPseudo + " joined " + gameName + " game");
            comSocket.write("/ JOINED");
        }
        catch (Exception e) {
            comSocket.write(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void startGame() throws IOException {
        if(gameHandler.getAdmin().equals(this.playerPseudo) == true){
            gameHandler.start();
        }else{
            System.out.println("not admin cannot start the game");
            comSocket.write("not admin cannot start the game"); // normalemet il n'y aura jamais ce cas
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
    }

    public void closeEverything(ComSocket comSocket) {
        try{
            comSocket.closeEverything();
            removeClientHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ComSocket getComSocket() {return comSocket;}
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
