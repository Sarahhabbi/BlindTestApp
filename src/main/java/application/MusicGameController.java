package application;

import Service.GameService;
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
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

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
    private String goodAnswer;
    private int numberOfRound = 1;
    private static String url = "/Users/habbi/Documents/L3-DANT/PROJETS_GROUPE/BlindTestApp/src/main/resources/music/bruno-mars-talking-to-the-moon-lyrics.mp3";
    private Media media;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playMusic(MusicGameController.url);
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
}
