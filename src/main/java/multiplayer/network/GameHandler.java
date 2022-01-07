package multiplayer.network;

import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler implements Runnable {
    public static ArrayList<GameHandler> games;
    public ArrayList<ClientHandler> players;    /* accès aux sockets de tous les joueurs de la partie */
    private String name ;
    private String admin ;

    private HashMap<String,Integer> scores = new HashMap<>(); // stock le nombre de bonne réponse --> {pseudo : score}

    public GameHandler(String name, String admin) {
        this.name = name;
        this.admin = admin;
    }


    @Override
    public void run() {
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
    public synchronized static GameHandler getGame(String name) throws Exception {
        if(exists(name) ==  true){
            for(GameHandler game : games){
                if(game.getName().equals(name) == true){
                    System.out.println(name + " found");
                    return game;
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
}
