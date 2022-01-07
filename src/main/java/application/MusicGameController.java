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
import models.Audio;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static application.BlindTestApplication.ROUND;

public class MusicGameController implements Initializable {
    @FXML
    private BorderPane paneGame;

    @FXML
    private TextField answerField;

    @FXML
    private Button submitBtn;
    @FXML

    private int counterRightAnswer = 0;
    private String goodAnswer = "bruno mars";
    private int numberOfRound = 1;


    private static String url = "../BlindTestApp/src/main/resources/music/bruno-mars-talking-to-the-moon-lyrics_d62CgrRx.mp3";
    private Media media;
    private MediaPlayer mediaPlayer;

    private final GameService gameService = new GameService();
    private ArrayList<Audio> songs = gameService.randomListaudio();
    private int nextSong = 0;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playMusic(MusicGameController.url);
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
            playMusic(url);
            Thread.sleep(5000);
            System.out.println("Next round !");
            System.out.println("Changing the image");
            changeSong();
            return 0;
        });
    }

    public void game() throws InterruptedException {
        int numberRounds = 0;
        while(numberRounds < ROUND ) {

            System.out.println("You have 30s to answer! ");
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

    public void playMusic(String url) {
        media = new Media(new File(url).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(23 * 0.01);
        mediaPlayer.play();
    }

    public void displayFinalResult(){

        HBox hbox = new HBox();
        Text textSent = new Text("Your final result is " + counterRightAnswer);
        hbox.setPadding(new Insets(5,5,5,10));
        hbox.setLayoutX(100.0);
        hbox.getChildren().add(textSent);

        paneGame.setCenter(hbox);
        paneGame.getChildren().remove(answerField);
        paneGame.getChildren().remove(submitBtn);
    }
    public void handlePlayerAnswer(ActionEvent event){

        if(numberOfRound <= ROUND){
            String playerAnswer = answerField.getText().toLowerCase();
            String rightOrWrongResponse;
            String color;   // background color depending on the answer wrong=red/ right=green

            if(playerAnswer.equals(goodAnswer) == true){
                System.out.println("good answer ->"  + playerAnswer);
                color = "#4ab721";
                rightOrWrongResponse = "Bonne réponse bien joué !";

                //increment counter for the player and display another image
                counterRightAnswer++;

                // next round
                answerField.clear();
                numberOfRound++;

                if(numberOfRound > ROUND){
                    displayFinalResult();
                }
            }
            else{
                System.out.println("bad answer ->"  + playerAnswer);
                color = "#ff2222";
                rightOrWrongResponse = "Mauvaise réponse, essayez de nouveau";
            }
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
            answerField.clear();
        }
        else
        {
            displayFinalResult();
        }
    }

    public void changeSong() throws InterruptedException {
        if(mediaPlayer!=null){
            System.out.println("stoping music");
            mediaPlayer.stop();
            Thread.sleep(500);
        }
        if(nextSong < songs.size())
        {
            // create new Image
            String url = songs.get(nextSong).getId();
            String newGoodAnswer = songs.get(nextSong).getAnswer();

            playMusic(url);

            // new good answer;
            this.goodAnswer = newGoodAnswer;
            System.out.println("new good answer = "+ goodAnswer);
            // for next call to this method
            nextSong++;
        }
        else{
            nextSong = 0;
        }
    }

}
