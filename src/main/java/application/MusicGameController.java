package application;

import Service.GameService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javazoom.jl.decoder.JavaLayerException;
import models.Audio;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.File;

import jaco.mp3.player.MP3Player;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static application.BlindTestApplication.ROUND;
import static spotify.songs.Spotify.getPlaylist;
import static spotify.songs.Spotify.getTrack;

public class MusicGameController implements Initializable {
    @FXML
    private BorderPane paneGame;

    @FXML
    private TextField answerField;

    @FXML
    private Button submitBtn;

    @FXML
    private Text rightAnswer;

    @FXML
    private Text score;
    /**********************************************************************/

//    private static String url = "../BlindTestApp/src/main/resources/music/bruno-mars-talking-to-the-moon-lyrics_d62CgrRx.mp3";

    /* for playlist and audio */
//    private final GameService gameService = new GameService();
//    private ArrayList<Audio> songs = gameService.randomListaudio(ROUND);
//    private Media media;
    private MP3Player mediaPlayer;
    private int nextSong = 1;

    private int numberOfRound = 1;
    
    /* for player answer */
    @FXML
    private String goodAnswer;
    private int counterRightAnswer = 0;
    HashMap<String, String> tracks = new HashMap<String, String>();


    /* for timer */
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    /**********************************************************************/
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String id = "1rylA7B1Y5YxOzs36plKgc";
        Playlist playlist = getPlaylist(id);
        Paging<PlaylistTrack> track = playlist.getTracks();
        PlaylistTrack[] trackUrl = track.getItems();

        for(int i = 0;i < trackUrl.length; i++){
            String idTrack = trackUrl[i].getTrack().getId();

            Track t = getTrack(idTrack);
            String goodAnswer = trackUrl[i].getTrack().getName().toLowerCase(); /* title of the track */
            System.out.println(goodAnswer);
            if(t!=null){
                tracks.put(goodAnswer, t.getPreviewUrl());
            }
        }

//        /* initialize first music */
//        playMusic(songs.get(0));
//        goodAnswer = songs.get(0).getAnswer();

        /* start the game */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    game();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Future<Integer> timer() {
        return executor.submit(() -> {
            changeSong();
            Thread.sleep(15000);
            System.out.println("Next round !");
            System.out.println("Changing the song");
            return 0;
        });
    }

    public void game() throws InterruptedException {
        int numberRounds = 0;
        while(numberRounds < ROUND ) {

            System.out.println("You have 30s to answer!");
            Future<Integer> future = timer();

            while(!future.isDone()) {

            }
            System.out.println("TIMER IS FINISHED...\n");
            numberRounds++;
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                displayFinalResultMusic();
            }
        });
    }

    public synchronized void displayFinalResultMusic(){
        System.out.println("final result music");
        paneGame.getChildren().remove(answerField);
        paneGame.getChildren().remove(submitBtn);
        HBox hbox = new HBox();
        Text textSent = new Text("Your final result is " + counterRightAnswer);
        hbox.setPadding(new Insets(5,5,5,10));
        hbox.setLayoutX(100.0);
        hbox.getChildren().add(textSent);

        paneGame.setCenter(hbox);
    }

    public synchronized void updateCurrentScore(){
        rightAnswer.setText("La bonne réponse était: \n\"" + goodAnswer+"\"");
        score.setText(String.valueOf(counterRightAnswer));
    }


    public void displayFinalResult(){
        HBox hbox = new HBox();
        Button goToGameType = new Button("Home Page");
        hbox.setPadding(new Insets(5,5,5,10));
        hbox.setLayoutX(100.0);
        hbox.getChildren().add(goToGameType);

        paneGame.getChildren().remove(answerField);
        paneGame.getChildren().remove(submitBtn);
        paneGame.getChildren().remove(score);
        paneGame.getChildren().remove(rightAnswer);
        paneGame.setCenter(hbox);
    }
    
    public void handlePlayerAnswer(ActionEvent event){

        if(numberOfRound <= ROUND){
            String playerAnswer = answerField.getText().toLowerCase();
            String rightOrWrongResponse;
            String color;   // background color depending on the answer wrong=red/ right=green

            if(playerAnswer.equals(this.goodAnswer) == true) {
                answerField.clear();
                System.out.println("good answer ->"  + playerAnswer);
                color = "#4ab721";
                rightOrWrongResponse = "Bonne réponse bien joué !";

                //increment counter for the player and display another image
                counterRightAnswer++;

                // next round
                numberOfRound++;

               /* if(numberOfRound > ROUND){
                    displayFinalResult();
                }*/
            }
            else{
                System.out.println("bad answer ->"  + playerAnswer);
                color = "#ff2222";
                rightOrWrongResponse = "Mauvaise réponse, essayez de nouveau";
            }
            alertMessageAnswer(rightOrWrongResponse, color); /* MAJ UI*/
            answerField.clear();
        }
        else
        {
            displayFinalResult();
        }
    }

    public void alertMessageAnswer(String rightOrWrongResponse, String color){
        HBox hbox = new HBox();
        Text textSent = new Text(rightOrWrongResponse);
        TextFlow textFlow = new TextFlow(textSent);

        //just styling the box for the message
        textFlow.setStyle("-fx-background-radius: 15px; -fx-color: rgb(255,255,255); -fx-background-color:" + color + ";");
        textFlow.setPadding(new Insets(5,10,5,10));
        textSent.setFill(Color.color(0.934,0.945,0.996));
        hbox.setPadding(new Insets(5,5,5,10));
        hbox.setLayoutX(100.0);
        hbox.getChildren().add(textFlow);

        paneGame.setBottom(hbox);
    }

    public void playMusic(Audio audio) {
//        media = new Media(new File(audio.getId()).toURI().toString());
//        mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.setVolume(23 * 0.01);
//        mediaPlayer.play();
        try {
            if(audio.getId()!=null){
                URL url = new URL(audio.getId());
                mediaPlayer = new MP3Player(url);
                mediaPlayer.play();
            }else{
                Audio newSong = findNewSong();
                playMusic(newSong);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public Audio findNewSong(){
        List keys = new ArrayList(tracks.keySet());

        int e = (int)(Math.random() * keys.size());
        String newGoodAnswer = (String)keys.get(e);

        String url = tracks.get(newGoodAnswer);
        Audio newSong = new Audio(url, newGoodAnswer);
        return newSong;
    }

    public void changeSong() throws InterruptedException {
        // stops the current music
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            System.out.println("next music in 5s");   // wait 5s before playing next
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    updateCurrentScore();
                }
            });
            Thread.sleep(5000);
        }
        // create new Audio and play it
//        String url = songs.get(nextSong).getId();
//        String newGoodAnswer = songs.get(nextSong).getAnswer();

        Audio newSong = findNewSong();

        playMusic(newSong);
        this.goodAnswer = newSong.getAnswer();

        System.out.println("new good answer = "+ this.goodAnswer);
        // for next call to this method
        nextSong++;
    }
}
