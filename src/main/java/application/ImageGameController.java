package application;

import Service.GameService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import models.MyImage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static application.BlindTestApplication.ROUND;

public class ImageGameController implements Initializable {
    @FXML
    private BorderPane paneGame;

    @FXML
    private ImageView imageBox;

    @FXML
    private TextField answerField;

    @FXML
    private Button homeBtn;

    @FXML
    private Button submitBtn;

    private final GameService gameService = new GameService();
    private int nextImage = 0;
    private ArrayList<MyImage> images = gameService.randomList(ROUND);

    private int counterRightAnswer = 0;
    private String goodAnswer;
    volatile private int numberOfRound = 1;

    private ExecutorService executor = Executors.newSingleThreadExecutor();


    private Stage stage;
    private Scene scene;
    private static Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeImage();
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
            Thread.sleep(15000);
            System.out.println("Next round !");
            System.out.println("Changing the image");
            changeImage();
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
                displayFinalResult();
            }
        });
    }

    public synchronized void displayFinalResult(){
        paneGame.getChildren().remove(imageBox);
        paneGame.getChildren().remove(answerField);
        paneGame.getChildren().remove(submitBtn);
        HBox hbox = new HBox();
        Text textSent = new Text("Your final result is " + counterRightAnswer);
        hbox.setPadding(new Insets(5,5,5,10));
        hbox.setLayoutX(100.0);
        hbox.getChildren().add(textSent);

        paneGame.setCenter(hbox);
    }

    public void handlePlayerAnswer(ActionEvent event){
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
        }
        else{
            System.out.println("bad answer ->"  + playerAnswer);
            color = "#ff2222";
            rightOrWrongResponse = "Mauvaise réponse, essayez de nouveau";
        }

        alertMessageAnswer(rightOrWrongResponse, color); /* MAJ UI*/

        answerField.clear();
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

    public void changeImage(){
        if(nextImage < images.size())
        {
            // create new Image
            String url = images.get(nextImage).getId();

            String newGoodAnswer = images.get(nextImage).getAnswer();
            Image image = new Image(url);

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

    @FXML
    public void goBackHome(ActionEvent event) throws IOException {
        try {
            String title = "BlindTest.IO";
            String pageToLoad = " ";

            if(event.getSource().equals(homeBtn))
            {
                pageToLoad = "/application/startPage.fxml";
            }

            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(pageToLoad)));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            System.out.println("fin debug");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
