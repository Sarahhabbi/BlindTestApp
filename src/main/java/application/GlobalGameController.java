package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.MyImage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static application.BlindTestApplication.ROUND;
import static javafx.geometry.Pos.CENTER_RIGHT;

public class GlobalGameController implements Initializable {
    @FXML
    private BorderPane paneGame;

    @FXML
    private ImageView imageBox;

    @FXML
    private TextField answerField;

    @FXML
    private Text minutesTimer;

    @FXML
    private Text secondsTimer;

    @FXML
    private Button startBtn;

    @FXML
    private Button submitBtn;

    private ArrayList<MyImage> images = BlindTestApplication.images;
    private int nextImage = 0;
    private String goodAnswer;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private int counterRightAnswer = 0;
    private int numberOfRound = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeImage();

        /*new Thread() {
            public void run() {
                System.out.println("execute game() in another thread");
                try {
                    game();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/

    }

//    public Future<Integer> timer() {
//        return executor.submit(() -> {
//            Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    System.out.println("Next round !");
//                    System.out.println("Changing the image");
//                    changeImage();
//                }
//            }, 5000);
//
//            return 0;
//        });
//    }

//    public void game() throws InterruptedException {
//        int numberRounds= 0;
//        while(numberRounds < ROUND ) {
//
//            System.out.println("You have 30s to answer! ");
//            Future<Integer> future = timer();
//
//            while(!future.isDone()) {
//                System.out.println("timer is not finished...");
//            }
//            System.out.println("TIMER IS FINISHED..");
//            System.out.println("TIMER IS FINISHED...");
//
//            numberRounds++;
//        }
//
//        // game is finished
//        displayFinalResult();
//    }

    public void displayFinalResult(){

        HBox hbox = new HBox();
        Text textSent = new Text("Your final result is " + counterRightAnswer);
        hbox.setPadding(new Insets(5,5,5,10));
        hbox.setLayoutX(100.0);
        hbox.getChildren().add(textSent);

        paneGame.setCenter(hbox);
        paneGame.getChildren().remove(imageBox);
        paneGame.getChildren().remove(answerField);
        paneGame.getChildren().remove(submitBtn);
    }

    public void handlePlayerAnswer(ActionEvent event){

        if(numberOfRound < ROUND){
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
                }else{
                    changeImage();
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

    public void changeImage(){
        if(nextImage < images.size())
        {
            // create new Image
            String url = images.get(nextImage).getUrl();
            String newGoodAnswer = images.get(nextImage).getAnswer();
            MyImage image = new MyImage(url, newGoodAnswer);

            // change the image
            imageBox.setImage(image);

            // new good answer;
            this.goodAnswer = newGoodAnswer;
            System.out.println("new good answer = "+ goodAnswer);
            // for next call to this method
            nextImage++;
        }
        else{
            nextImage = 0;
        }

    }

}
