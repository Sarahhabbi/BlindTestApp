package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GlobalGameController implements Initializable {

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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
