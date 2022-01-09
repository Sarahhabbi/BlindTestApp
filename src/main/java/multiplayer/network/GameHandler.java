package multiplayer.network;

import Service.GameService;
import models.Audio;
import models.MyImage;
import models.PlayerAnswer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.*;

public class GameHandler extends Thread {
    public static HashMap<String,GameHandler> games=new HashMap<>();

    public ArrayList<ClientHandler> players = new ArrayList<ClientHandler>();    /* accès aux sockets de tous les joueurs de la partie */
    private HashMap<String,Integer> scores = new HashMap<>();

    public final GameService gameservice;
    public ArrayList<MyImage> images;
    public ArrayList<Audio> audios;

    private final String name ;
    private final String admin ;
    private boolean start;
    private int round;
    private boolean isAudio;

    private static ArrayList<ExecutorService> executor = new ArrayList<>();

    public Future<PlayerAnswer> RoundImages(int i, int ID, CyclicBarrier barrier, ClientHandler client) {
        return executor.get(i).submit(() -> {
            try {
                ComSocket s=client.getComSocket();
                s.write("Thread #" + i + " is waiting at the barrier.");
                barrier.await();
                s.write("La partie "+ID+" va commencer");
                s.writeImage(images.get(i));
                Thread.sleep(5000);
                PlayerAnswer answer=(PlayerAnswer) s.readPlayerAnswer();
                s.write("FIN DU TOUR");
                if(ID==4) {
                    s.write("FIN DU TOUR");
                }
                return answer;
                //Code a faire
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                System.out.println("Barrier is broken.");
            }
            return null;
        });

    }

    public Future<PlayerAnswer> Roundaudios(int i, int ID, CyclicBarrier barrier, ClientHandler client) {
        return executor.get(i).submit(() -> {
            try {
                ComSocket s=client.getComSocket();
                s.write("Thread #" + i + " is waiting at the barrier.");
                barrier.await();
                s.write("La partie "+ID+" va commencer");
                s.writeAudio(audios.get(i));
                Thread.sleep(5000);
                PlayerAnswer answer=(PlayerAnswer) s.readPlayerAnswer();
               s.write("FIN DU TOUR");
                if(ID==4) {
                    s.write("FIN DU TOUR");
                }
                return answer;
                //Code a faire
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                System.out.println("Barrier is broken.");
                e.printStackTrace();
            }
            return null;
        });
    }

    public GameHandler(String name, String admin,int round) {
        this.name = name;
        this.admin = admin;
        this.start=false;
        this.round=round;
        this.isAudio=false;
        this.gameservice=new GameService();
        this.images=gameservice.randomList(5);
        GameHandler.games.put(name,this);
    }

    public GameHandler(String name, String admin,boolean isAudio,int round) {
        this.name = name;
        this.admin = admin;
        this.round=round;
        this.start=false;
        this.isAudio=false;
        this.gameservice=new GameService();
        if(isAudio){
            this.audios=gameservice.randomListaudio(5);
        }else{
            this.images=gameservice.randomList(5);
        }
        GameHandler.games.put(name,this);
    }


    @Override
    public void run() {
        if(isAudio){
            playAudios();
        }
        else{
            playImages();
        }
    }

    public ArrayList<String> playImages(){
        int n= players.size();
        System.out.println("Il y a " + n + " clients");
        Future<PlayerAnswer>[] t=new Future[n];

        for (int r = 0; r < this.round; r++) {
            Runnable barrierAction = () -> System.out.println("Round ");
            CyclicBarrier barrier = new CyclicBarrier(4, barrierAction);
            n= players.size();
            for (int i = 0; i < n; i++) {
                t[i] = RoundImages(i, r, barrier, players.get(i));
            }
            ArrayList<PlayerAnswer> results=new ArrayList<>();
            for (int j = 0; j < n; j++) {
                try {
                    results.add(t[j].get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            addRound(results);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return getWinnerGame();

    }


    public ArrayList<String> playAudios(){
        int n= players.size();
        System.out.println("Il y a " + n + " clients");
        Future<PlayerAnswer>[] t=new Future[n];

        for (int r = 0; r < this.round; r++) {
            Runnable barrierAction = () -> System.out.println("Round ");
            CyclicBarrier barrier = new CyclicBarrier(4, barrierAction);
            n= players.size();
            for (int i = 0; i < n; i++) {
                t[i] = Roundaudios(i, r, barrier, players.get(i));
            }
            ArrayList<PlayerAnswer> results=new ArrayList<>();
            for (int j = 0; j < n; j++) {
                try {
                    results.add(t[j].get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            addRound(results);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return getWinnerGame();
    }

    private void addRound(ArrayList<PlayerAnswer> results) {
        ArrayList<PlayerAnswer> bestanswers=new ArrayList<>();
        PlayerAnswer a=results.get(0);
        long time=a.getAnswerTime();
        for(int i=1;i<results.size();i++){
            a=results.get(i);
            if(a.getAnswerTime()==time){
                bestanswers.add(a);
            }
            if(a.getAnswerTime()<time){
                time=a.getAnswerTime();
                bestanswers.clear();
                bestanswers.add(a);
            }

        }
        for(int j=0;j< bestanswers.size();j++){
            a=bestanswers.get(j);
            scores.put(a.getPseudoPlayer(),scores.get(a)+1);
        }
    }


    public synchronized static boolean exists (String gameName){
        return games.containsKey(gameName);
    }

    public synchronized void addPlayer(ClientHandler player){
        System.out.println("rajout d'un joueur");
        this.scores.put(player.getPlayerPseudo(),0);
        this.players.add(player);
    }
    public void remove(ClientHandler player){
        this.players.remove(player);
        this.scores.remove(player);
    }

    public synchronized static GameHandler getGame(String name) throws Exception {
        if(games.containsKey(name) == true){
            throw new Exception("/ NOT_EXISTS_GAMENAME");
        }
        GameHandler game=games.get(name);
        if(game.start==true){
            throw new Exception("/ STARTED");
        }
        return game;
    }

    public synchronized static GameHandler addGame(String name, String admin,boolean isaudio,int round) throws Exception{
        if(games.containsKey(name)){
            throw new Exception("/ EXISTS_GAMENAME");
        }else{
            GameHandler newGame = new GameHandler(name,admin,isaudio,round);
            return newGame;
        }
    }

    public synchronized void deleteGame(String gameName){
        GameHandler game=games.get(gameName);
        if(game!=null){
            System.out.println(gameName + " deleted");
            games.remove(gameName);
        }
        for(ClientHandler player : players){
            System.out.println(player.getPlayerPseudo() + " a quitté");
            player.setJoinedGame(null);
        }
    }

    public ArrayList<String> getWinnerGame(){
        ArrayList<String> winner =null;
        int winnerScore=0;
        for (Map.Entry<String, Integer> pair: scores.entrySet()){
            if(pair.getValue() > winnerScore){
                winner.clear();
                winner.add(pair.getKey());
                winnerScore=pair.getValue();
            }
            else if(winnerScore == pair.getValue()){
                winner.add(pair.getKey());
            }
        }
        return winner ;
    }



    public String getname() {return name;}
    public HashMap<String, Integer> getScores() {return scores;}
    public void setScores(HashMap<String, Integer> scores) {this.scores = scores;}
    public void giveAnswer(String answer) {}
    public ArrayList<ClientHandler> getPlayers() {return players;}
    public String getAdmin() {return admin;}
    public boolean isStart() {return start;}



}
