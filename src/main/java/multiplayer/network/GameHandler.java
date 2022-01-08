package multiplayer.network;

import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler implements Runnable {
    public static ArrayList<GameHandler> games;
    private ArrayList<ClientHandler> players;    /* accès aux sockets de tous les joueurs de la partie */
    private String name ;
    private String admin ;
    private boolean start;
    private HashMap<String,Integer> scores = new HashMap<>(); // stock le nombre de bonne réponse --> {pseudo : score}

    public GameHandler(String name, String admin) {
        this.name = name;
        this.admin = admin;
        this.start = false;
    }

    @Override
    public void run() {
       /* for(int round=0;round<5;round++) {
            Runnable barrierAction = () -> System.out.println("La partie va commencer");
            CyclicBarrier barrier = new CyclicBarrier(3, barrierAction);
            for (int i = 0; i <players.size(); i++) {
                Worker t = new Worker(i, barrier,players.get(i).getWriter());
                t.start(); //ils envoient l'image
            }
        }

        System.out.println("Il y a "+ clientHandlers.size()+" clients");
        if(clientHandlers.size()==4) {
            Runnable barrierAction = () -> System.out.println("Round ");
            CyclicBarrier barrier = new CyclicBarrier(4, barrierAction);
            for (int i = 0; i < clientHandlers.size(); i++) {
                Worker t = new Worker(i, barrier, clientHandlers.get(i));
                t.start(); //ils envoient l'image
                System.out.println(clientHandlers.get(i).toString());
            }
        } */

        /* while loop pour les round*/
            /* envoi image à tous les players*/
            /* thread sleep timer*/
            /* on envoi "end round" à tous les players */
            /* on récupere les reponses de chaque joueur et on vérifie qui a gagné ce tour la et MAJ scores */
            /* thread spleep */
        /* une fois tous les round terminés on récupère dans la HashMap le pseudo de celui qui a le max score ->
         autre methode qui parcours la HashMap score*/
    }

    public synchronized static boolean exists (String gameName){
        for(GameHandler game : games){
            if(game.getName().equals(gameName) == true){
                return true;
            }
        }
        return false;
    }

    public synchronized void addPlayer(ClientHandler player){
            this.players.add(player);
    }
    public static void remove(ClientHandler player){
        games.remove(player);
    }
    public synchronized static GameHandler getGame(String name) throws Exception {
        if(exists(name) ==  true){
            for(GameHandler game : games){
                if(game.getName().equals(name) == true && game.start==false){
                    System.out.println(name + " found");
                    return game;
                }
                else{
                    throw new Exception("La partie demandée a déjà commencé, veuillez choisir une autre partie");
                }
            }
        }else{
            throw new Exception("La partie demandée n'existe pas");
        }
        return null;
    }

    public synchronized static GameHandler addGame(String name, String admin) throws Exception{
        if(!exists(name)){
            GameHandler newGame = new GameHandler(name,admin);
            games.add(newGame);
            return newGame;
        }else{
            throw new Exception("La partie existe déjà");
        }
    }

    public synchronized void deleteGame(String gameName){
        for(GameHandler game : games){
            if(game.getName().equals(gameName)){
                System.out.println(gameName + " deleted");
                games.remove(game);
            }
        }
        for(ClientHandler player : this.players){
            player.setJoinedGame(null);
        }

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public HashMap<String, Integer> getScores() {
        return scores;
    }
    public void setScores(HashMap<String, Integer> scores) {
        this.scores = scores;
    }

    public void startGame() {

    }

    public void giveAnswer(String answer) {
    }

    public ArrayList<ClientHandler> getPlayers() {
        return players;
    }

    public String getAdmin() {
        return admin;
    }

    public boolean isStart() {
        return start;
    }



}
